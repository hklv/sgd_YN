package org.activiti.engine.impl.persistence;

import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.invoker.MethodInvoker;
import org.apache.ibatis.reflection.property.PropertyNamer;

public class LiybTest {
	private Map<String, Invoker> getMethods = new HashMap<String, Invoker>();
	private Map<String, Class<?>> getTypes = new HashMap<String, Class<?>>();

	private void addMethodConflict(
			Map<String, List<Method>> conflictingMethods, String name,
			Method method) {

		List<Method> list = conflictingMethods.get(name);

		if (list == null) {

			list = new ArrayList<Method>();

			conflictingMethods.put(name, list);

		}

		list.add(method);

	}

	public void addGetMethods(Class<?> cls) {

		Map<String, List<Method>> conflictingGetters = new HashMap<String, List<Method>>();

		Method[] methods = getClassMethods(cls);

		for (Method method : methods) {

			String name = method.getName();
			

			if (name.startsWith("get") && name.length() > 3) {

				if (method.getParameterTypes().length == 0) {
					System.out.println("method_name = "+ name);
					name = PropertyNamer.methodToProperty(name);

					addMethodConflict(conflictingGetters, name, method);
					 
				}

			} else if (name.startsWith("is") && name.length() > 2) {

				if (method.getParameterTypes().length == 0) {
					System.out.println("method_name = "+ name);
					name = PropertyNamer.methodToProperty(name);

					addMethodConflict(conflictingGetters, name, method);
					

				}

			}

		}

		resolveGetterConflicts(conflictingGetters);

	}

	private void resolveGetterConflicts(
			Map<String, List<Method>> conflictingGetters) {

		for (String propName : conflictingGetters.keySet()) {

			List<Method> getters = conflictingGetters.get(propName);

			Iterator<Method> iterator = getters.iterator();

			Method firstMethod = iterator.next();
			System.out.println(propName);
			if (getters.size() == 1) {

				addGetMethod(propName, firstMethod);

			} else {

				Method getter = firstMethod;

				Class<?> getterType = firstMethod.getReturnType();

				while (iterator.hasNext()) {

					Method method = iterator.next();

					Class<?> methodType = method.getReturnType();

					if (methodType.equals(getterType)) {

						throw new ReflectionException(
								"Illegal overloaded getter method with ambiguous type for property "

										+ propName
										+ " in class "
										+ firstMethod.getDeclaringClass()

										+ ".  This breaks the JavaBeans "
										+ "specification and can cause unpredicatble results.");

					} else if (methodType.isAssignableFrom(getterType)) {

						// OK getter type is descendant

					} else if (getterType.isAssignableFrom(methodType)) {

						getter = method;

						getterType = methodType;

					} else {

						throw new ReflectionException(
								"Illegal overloaded getter method with ambiguous type for property "

										+ propName
										+ " in class "
										+ firstMethod.getDeclaringClass()

										+ ".  This breaks the JavaBeans "
										+ "specification and can cause unpredicatble results.");

					}

				}

				addGetMethod(propName, getter);

			}

		}

	}

	private void addGetMethod(String name, Method method) {

		if (isValidPropertyName(name)) {

			getMethods.put(name, new MethodInvoker(method));

			getTypes.put(name, method.getReturnType());

		}

	}

	private boolean isValidPropertyName(String name) {
		return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class"
				.equals(name));

	}

	private Method[] getClassMethods(Class<?> cls) {

		HashMap<String, Method> uniqueMethods = new HashMap<String, Method>();

		Class<?> currentClass = cls;

		while (currentClass != null) {

			addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());

			// we also need to look for interface methods -

			// because the class may be abstract

			Class<?>[] interfaces = currentClass.getInterfaces();

			for (Class<?> anInterface : interfaces) {

				addUniqueMethods(uniqueMethods, anInterface.getMethods());

			}

			currentClass = currentClass.getSuperclass();

		}

		Collection<Method> methods = uniqueMethods.values();

		return methods.toArray(new Method[methods.size()]);

	}

	private void addUniqueMethods(HashMap<String, Method> uniqueMethods,
			Method[] methods) {

		for (Method currentMethod : methods) {

			if (!currentMethod.isBridge()) {

				String signature = getSignature(currentMethod);

				// check to see if the method is already known

				// if it is known, then an extended class must have

				// overridden a method

				if (!uniqueMethods.containsKey(signature)) {

					if (canAccessPrivateMethods()) {

						try {

							currentMethod.setAccessible(true);

						} catch (Exception e) {

							// Ignored. This is only a final precaution, nothing
							// we can do.

						}

					}

					uniqueMethods.put(signature, currentMethod);

				}

			}

		}

	}

	private static boolean canAccessPrivateMethods() {

		try {

			SecurityManager securityManager = System.getSecurityManager();

			if (null != securityManager) {

				securityManager.checkPermission(new ReflectPermission(
						"suppressAccessChecks"));

			}

		} catch (SecurityException e) {

			return false;

		}

		return true;

	}

	private String getSignature(Method method) {

		StringBuilder sb = new StringBuilder();

		Class<?> returnType = method.getReturnType();

		if (returnType != null) {

			sb.append(returnType.getName()).append('#');

		}

		sb.append(method.getName());

		Class<?>[] parameters = method.getParameterTypes();

		for (int i = 0; i < parameters.length; i++) {

			if (i == 0) {

				sb.append(':');

			} else {

				sb.append(',');

			}

			sb.append(parameters[i].getName());

		}

		return sb.toString();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LiybTest t = new LiybTest();
		t.addGetMethods(ExecutionEntity.class);

	}

}
