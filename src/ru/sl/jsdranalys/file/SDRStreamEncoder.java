package ru.sl.jsdranalys.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ru.sl.jsdranalys.common.Multiplicity;

/**
 * Класс чтения бинарных данных из файла додетектоной записи
 * формат данных определяется перечислением CountdownType
 * выходные данные представлены в формате чисел с плавающей точкой
 * для представления комплексных чисел используется
 * двумерный массив {{real,image},{real,image},...{real,image}}
 * действительные данные представлены одномерным массивом
 * 
 * @author SergeyLomsky
 */
public class SDRStreamEncoder {

	protected CountdownType countdownType;
	private ByteOrder byteOrder;
	private FileInputStream fin;
	private long position = 0;
	
	/**
	 * Установки параметров чтения данных
	 * 
	 * @param countdownType - тип и формат представляемых чисел
	 * @param byteOrder - порядок следования байт
	 * @param fin - поток данных
	 */
	public SDRStreamEncoder(CountdownType countdownType, ByteOrder byteOrder, FileInputStream fin) {
		this.countdownType = countdownType;
		this.byteOrder = byteOrder;
		this.fin = fin;
	}

	/**
	 * метод чтения данных из файла
	 * 
	 * @param len - количество отсчетов (комплексный отсчет представлен двумя числами)
	 * @return - массив данных
	 * @throws IOException
	 */
	public Object encodeData(int len) throws IOException {
		return countdownType.getMultiplicity() == Multiplicity.COMPLEX ? inComplexData(len) : inRealData(len);
	}
	
	/**
	 * проверка на окончание файла
	 * 
	 * @param len - количество данных для чтения
	 * @return
	 * @throws IOException
	 */
	public boolean isEOF(int len) throws IOException {
		return fin.available()<len * countdownType.getAtomLength();
	}

	/**
	 * Установка указателя на позицию в файле
	 * 
	 * @param position - значение позиции
	 * @return - возвращает количество отсчетов до конца файла
	 * @throws IOException 
	 */
	public long setPosition(long position) throws IOException {
		this.position = (long) (position * countdownType.getAtomLength());
		return (long) fin.available()/countdownType.getAtomLength();
	}
	
	/**
	 * метод чтения данных
	 * 
	 * @param out -  массив куда будут помещены прочитанные данные
	 * @throws IOException
	 */
	private void inBuffer(double[]... out) throws IOException {
		int len = out[0].length;
		ByteBuffer buffer = ByteBuffer.allocateDirect(len * countdownType.getAtomLength());
		fin.getChannel().read(buffer, position);
		buffer.order(byteOrder);
		buffer.position(0);
		for (int counter = 0; counter < len; counter++) {
			for (double[] d : out) {
				d[counter] = countdownType.getValue(buffer);
			}
		}
	}

	//чтение действительных данных
	private Object inRealData(int len) throws IOException {
		double[] inData = new double[len];
		inBuffer(inData);
		return inData;
	}

	//чтение комплексных данных
	private Object inComplexData(int len) throws IOException {
		double[][] inData = new double[2][len];
		inBuffer(inData);
		return inData;
	}


	public static void main(String[] args) {
		
		String fileName = args[0];
		ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
		CountdownType countdownType = CountdownType.LONGINTCOMPLEX;
		FileInputStream stream = null;
		try {
			
			stream = new FileInputStream(fileName);
			SDRStreamEncoder encoder =new SDRStreamEncoder(countdownType, byteOrder, stream);
			
			double[][] out = (double[][])encoder.encodeData(32768);
			for (int i = 0; i < out[0].length; i++) {
				System.out.print(i);
				for (double[] d : out) {
					System.out.print(" "+d[i]);
				}
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null)
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
		}
	}

}
