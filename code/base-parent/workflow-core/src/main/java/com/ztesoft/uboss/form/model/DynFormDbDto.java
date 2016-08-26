package com.ztesoft.uboss.form.model;

public class DynFormDbDto {

    private String tableName;
    //
    private String columnName;
    //
    private String columnDefault;
    //
    private String isNullable;
    //
    private String columnDataType;
    //
    private int characterLength;
    //
    private float numericPrecision;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public int getCharacterLength() {
        return characterLength;
    }

    public void setCharacterLength(int characterLength) {
        this.characterLength = characterLength;
    }

    public float getNumericPrecision() {
        return numericPrecision;
    }

    public void setNumericPrecision(float numericPrecision) {
        this.numericPrecision = numericPrecision;
    }


}
