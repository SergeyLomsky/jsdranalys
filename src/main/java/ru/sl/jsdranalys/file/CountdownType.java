package ru.sl.jsdranalys.file;

import java.nio.ByteBuffer;

import ru.sl.jsdranalys.common.Annotation;
import ru.sl.jsdranalys.common.Multiplicity;

/**
 * Тип данных и принадлежность к множеству
 * Выходные данны представлены типом double
 * это связано с дальнейшей обработкой информации
 * Предполагается в проекте использовать библиотеку
 * CommonMath3, в которой в качестве исходной
 * информации используется формат данных
 * с плавающей точкой
 * 
 * @author SergeyLomsky
 */
public enum CountdownType implements Annotation {

    SHORTREAL(1, Multiplicity.REAL, "8 бит - действительные") {
        public double getValue(ByteBuffer buffer) {
            return (double) buffer.get();
        }
    },
    INTREAL(2, Multiplicity.REAL, "16 бит - действительные") {
        public double getValue(ByteBuffer buffer) {
            return (double) buffer.getChar();
        }
    },
    LONGINTREAL(4, Multiplicity.REAL, "32 бита - действительные") {
        public double getValue(ByteBuffer buffer) {
            return (double) buffer.getInt();
        }
    },
    SHORTCOMPLEX(1, Multiplicity.COMPLEX, "8 бит - комплексные") {
        public double getValue(ByteBuffer buffer) {
            return (double) buffer.get();
        }
    },
    INTCOMPLEX(2, Multiplicity.COMPLEX, "16 бит - комплексные") {
        public double getValue(ByteBuffer buffer) {
            return (double) buffer.getChar();
        }
    },
    LONGINTCOMPLEX(4, Multiplicity.COMPLEX, "32 бита - комплексные") {
        public double getValue(ByteBuffer buffer) {
            return (double) buffer.getInt();
        }
    };

    private Multiplicity multiplicity;
    private int length;
    private String annotation;

    private CountdownType(int length, Multiplicity multiplicity, String annotation) {
        this.length = length;
        this.multiplicity = multiplicity;
        this.annotation = annotation;
    }

    @Override
    public String getDisplayName() {
        return annotation;
    }

    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    public int getAtomLength() {
        return length * (multiplicity.ordinal() + 1);
    }

    /**
     * читает из буфера заданное количество байт и преобразует в тип double
     * 
     * @param buffer
     * @return
     */
    public abstract double getValue(ByteBuffer buffer);

}
