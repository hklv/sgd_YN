package org.activiti.engine.impl.persistence;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.invoker.GetFieldInvoker;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.invoker.MethodInvoker;
import org.apache.ibatis.reflection.invoker.SetFieldInvoker;
import org.apache.ibatis.reflection.property.PropertyNamer;

public class ReflectTest {
	private static boolean classCacheEnabled = true;
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final Map<Class<?>, ReflectTest> REFLECTOR_MAP = Collections
			.synchronizedMap(new HashMap());
	private Class<?> type;
	private String[] readablePropertyNames = EMPTY_STRING_ARRAY;
	private String[] writeablePropertyNames = EMPTY_STRING_ARRAY;
	private Map<String, Invoker> setMethods = new HashMap();
	private Map<String, Invoker> getMethods = new HashMap();
	private Map<String, Class<?>> setTypes = new HashMap();
	private Map<String, Class<?>> getTypes = new HashMap();
	private Constructor<?> defaultConstructor;
	private Map<String, String> caseInsensitivePropertyMap = new HashMap();

	public ReflectTest() {

	}

	private ReflectTest(Class<?> clazz) {
		this.type = clazz;
		addDefaultConstructor(clazz);
		addGetMethods(clazz);
		addSetMethods(clazz);
		addFields(clazz);
		this.readablePropertyNames = ((String[]) this.getMethods.keySet()
				.toArray(new String[this.getMethods.keySet().size()]));
		this.writeablePropertyNames = ((String[]) this.setMethods.keySet()
				.toArray(new String[this.setMethods.keySet().size()]));
		for (String propName : this.readablePropertyNames) {
			this.caseInsensitivePropertyMap.put(
					propName.toUpperCase(Locale.ENGLISH), propName);
		}
		for (String propName : this.writeablePropertyNames)
			this.caseInsensitivePropertyMap.put(
					propName.toUpperCase(Locale.ENGLISH), propName);
	}

	private void addDefaultConstructor(Class<?> clazz) {
		Constructor[] consts = clazz.getDeclaredConstructors();
		for (Constructor constructor : consts)
			if (constructor.getParameterTypes().length == 0) {
				if (canAccessPrivateMethods())
					try {
						constructor.setAccessible(true);
					} catch (Exception e) {
					}
				if (constructor.isAccessible())
					this.defaultConstructor = constructor;
			}
	}

	private void addGetMethods(Class<?> cls) {
		Method[] methods = getClassMethods(cls);
		for (Method method : methods) {
			String name = method.getName();
			if ((name.startsWith("get")) && (name.length() > 3)) {
				if (method.getParameterTypes().length == 0) {
					System.out.println(name);
					name = PropertyNamer.methodToProperty(name);
					addGetMethod(name, method);
				}
			} else if ((name.startsWith("is")) && (name.length() > 2)
					&& (method.getParameterTypes().length == 0)) {
				System.out.println(name);
				name = PropertyNamer.methodToProperty(name);

				addGetMethod(name, method);
			}
		}
		int a = 0;
	}

	private void addGetMethod(String name, Method method) {
		if (isValidPropertyName(name)) {
			this.getMethods.put(name, new MethodInvoker(method));
			this.getTypes.put(name, method.getReturnType());

		}
	}

	private void addSetMethods(Class<?> cls) {
		Map conflictingSetters = new HashMap();
		Method[] methods = getClassMethods(cls);
		for (Method method : methods) {
			String name = method.getName();
			if ((name.startsWith("set")) && (name.length() > 3)
					&& (method.getParameterTypes().length == 1)) {
				name = PropertyNamer.methodToProperty(name);
				addSetterConflict(conflictingSetters, name, method);
			}
		}

		resolveSetterConflicts(conflictingSetters);
	}

	private void addSetterConflict(
			Map<String, List<Method>> conflictingSetters, String name,
			Method method) {
		List list = (List) conflictingSetters.get(name);
		if (list == null) {
			list = new ArrayList();
			conflictingSetters.put(name, list);
		}
		list.add(method);
	}

	private void resolveSetterConflicts(
			Map<String, List<Method>> conflictingSetters) {
		for (String propName : conflictingSetters.keySet()) {
			List setters = (List) conflictingSetters.get(propName);
			Method firstMethod = (Method) setters.get(0);
			if (setters.size() == 1) {
				addSetMethod(propName, firstMethod);
			} else {
				Class expectedType = (Class) this.getTypes.get(propName);
				if (expectedType == null) {
					throw new ReflectionException(
							"Illegal overloaded setter method with ambiguous type for property "
									+ propName
									+ " in class "
									+ firstMethod.getDeclaringClass()
									+ ".  This breaks the JavaBeans "
									+ "specification and can cause unpredicatble results.");
				}

				Iterator methods = setters.iterator();
				Method setter = null;
				while (methods.hasNext()) {
					Method method = (Method) methods.next();
					if ((method.getParameterTypes().length == 1)
							&& (expectedType
									.equals(method.getParameterTypes()[0]))) {
						setter = method;
						break;
					}
				}
				if (setter == null) {
					throw new ReflectionException(
							"Illegal overloaded setter method with ambiguous type for property "
									+ propName
									+ " in class "
									+ firstMethod.getDeclaringClass()
									+ ".  This breaks the JavaBeans "
									+ "specification and can cause unpredicatble results.");
				}

				addSetMethod(propName, setter);
			}
		}
	}

	private void addSetMethod(String name, Method method) {
		if (isValidPropertyName(name)) {
			this.setMethods.put(name, new MethodInvoker(method));
			this.setTypes.put(name, method.getParameterTypes()[0]);
		}
	}

	private void addFields(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (canAccessPrivateMethods())
				try {
					field.setAccessible(true);
				} catch (Exception e) {
				}
			if (field.isAccessible()) {
				if (!this.setMethods.containsKey(field.getName())) {
					addSetField(field);
				}
				if (!this.getMethods.containsKey(field.getName())) {
					addGetField(field);
				}
			}
		}
		if (clazz.getSuperclass() != null)
			addFields(clazz.getSuperclass());
	}

	private void addSetField(Field field) {
		if (isValidPropertyName(field.getName())) {
			this.setMethods.put(field.getName(), new SetFieldInvoker(field));
			this.setTypes.put(field.getName(), field.getType());
		}
	}

	private void addGetField(Field field) {
		if (isValidPropertyName(field.getName())) {
			this.getMethods.put(field.getName(), new GetFieldInvoker(field));
			this.getTypes.put(field.getName(), field.getType());
		}
	}

	private boolean isValidPropertyName(String name) {
		return (!name.startsWith("$")) && (!"serialVersionUID".equals(name))
				&& (!"class".equals(name));
	}

	private Method[] getClassMethods(Class<?> cls) {
		HashMap uniqueMethods = new HashMap();
		Class currentClass = cls;
		while (currentClass != null) {
			addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());

			Class[] interfaces = currentClass.getInterfaces();
			for (Class anInterface : interfaces) {
				addUniqueMethods(uniqueMethods, anInterface.getMethods());
			}

			currentClass = currentClass.getSuperclass();
		}

		Collection methods = uniqueMethods.values();

		return (Method[]) methods.toArray(new Method[methods.size()]);
	}

	private void addUniqueMethods(HashMap<String, Method> uniqueMethods,
			Method[] methods) {
		for (Method currentMethod : methods)
			if (!currentMethod.isBridge()) {
				String signature = getSignature(currentMethod);

				if (!uniqueMethods.containsKey(signature)) {
					if (canAccessPrivateMethods()) {
						try {
							currentMethod.setAccessible(true);
						} catch (Exception e) {
						}
					}
					uniqueMethods.put(signature, currentMethod);
				}
			}
	}

	private String getSignature(Method method) {
		StringBuffer sb = new StringBuffer();
		sb.append(method.getName());
		Class[] parameters = method.getParameterTypes();
		for (int i = 0; i < parameters.length; i++) {
			if (i == 0)
				sb.append(':');
			else {
				sb.append(',');
			}
			sb.append(parameters[i].getName());
		}
		return sb.toString();
	}

	private static boolean canAccessPrivateMethods() {
		try {
			SecurityManager securityManager = System.getSecurityManager();
			if (null != securityManager)
				securityManager.checkPermission(new ReflectPermission(
						"suppressAccessChecks"));
		} catch (SecurityException e) {
			return false;
		}
		return true;
	}

	public Class<?> getType() {
		return this.type;
	}

	public Constructor<?> getDefaultConstructor() {
		if (this.defaultConstructor != null) {
			return this.defaultConstructor;
		}
		throw new ReflectionException("There is no default constructor for "
				+ this.type);
	}

	public Invoker getSetInvoker(String propertyName) {
		Invoker method = (Invoker) this.setMethods.get(propertyName);
		if (method == null) {
			throw new ReflectionException(
					"There is no setter for property named '" + propertyName
							+ "' in '" + this.type + "'");
		}
		return method;
	}

	public Invoker getGetInvoker(String propertyName) {
		Invoker method = (Invoker) this.getMethods.get(propertyName);
		if (method == null) {
			throw new ReflectionException(
					"There is no getter for property named '" + propertyName
							+ "' in '" + this.type + "'");
		}
		return method;
	}

	public Class<?> getSetterType(String propertyName) {
		Class clazz = (Class) this.setTypes.get(propertyName);
		if (clazz == null) {
			throw new ReflectionException(
					"There is no setter for property named '" + propertyName
							+ "' in '" + this.type + "'");
		}
		return clazz;
	}

	public Class<?> getGetterType(String propertyName) {
		Class clazz = (Class) this.getTypes.get(propertyName);
		if (clazz == null) {
			throw new ReflectionException(
					"There is no getter for property named '" + propertyName
							+ "' in '" + this.type + "'");
		}
		return clazz;
	}

	public String[] getGetablePropertyNames() {
		return this.readablePropertyNames;
	}

	public String[] getSetablePropertyNames() {
		return this.writeablePropertyNames;
	}

	public boolean hasSetter(String propertyName) {
		return this.setMethods.keySet().contains(propertyName);
	}

	public boolean hasGetter(String propertyName) {
		return this.getMethods.keySet().contains(propertyName);
	}

	public String findPropertyName(String name) {
		return (String) this.caseInsensitivePropertyMap.get(name
				.toUpperCase(Locale.ENGLISH));
	}

	public static ReflectTest forClass(Class<?> clazz) {
		if (classCacheEnabled) {
			synchronized (clazz) {
				ReflectTest cached = (ReflectTest) REFLECTOR_MAP.get(clazz);
				if (cached == null) {
					cached = new ReflectTest(clazz);
					REFLECTOR_MAP.put(clazz, cached);
				}
				return cached;
			}
		}
		return new ReflectTest(clazz);
	}

	public static void setClassCacheEnabled(boolean classCacheEnabled) {
		classCacheEnabled = classCacheEnabled;
	}

	public static boolean isClassCacheEnabled() {
		return classCacheEnabled;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReflectTest t = new ReflectTest();
		t.addGetMethods(ExecutionEntity.class);

	}

}
