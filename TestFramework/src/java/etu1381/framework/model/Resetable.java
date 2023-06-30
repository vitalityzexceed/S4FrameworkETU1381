package etu1381.framework.model;

import java.lang.reflect.Field;

public class Resetable 
{
    
    public void resetAttributes() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (fieldType.isArray()) {
                    field.set(this, null);
                } else if (fieldType.isPrimitive()) {
                    field.set(this, getDefaultPrimitiveValue(fieldType));
                } else {
                    field.set(this, null);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }    

    private Object getDefaultPrimitiveValue(Class<?> fieldType) {
        if (fieldType == boolean.class) {
            return false;
        } else if (fieldType == byte.class) {
            return (byte) 0;
        } else if (fieldType == short.class) {
            return (short) 0;
        } else if (fieldType == int.class) {
            return 0;
        } else if (fieldType == long.class) {
            return 0L;
        } else if (fieldType == float.class) {
            return 0.0f;
        } else if (fieldType == double.class) {
            return 0.0;
        } else if (fieldType == char.class) {
            return '\u0000';
        } else {
            throw new IllegalArgumentException("Unsupported primitive type: " + fieldType);
        }
    }
}
