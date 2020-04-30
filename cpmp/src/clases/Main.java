package clases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import structs.Node;


public class Main 
{
	
	private static ArrayList posibles=new ArrayList();
	public static void main (String Args[]) throws IOException, CloneNotSupportedException
	{
	
		double k=0.5;
		SelectFile sF=new SelectFile();
		ArrayList instances=sF.SelectFile();
		
	
		while (instances.size()>0)
		{
			File dir=(File)instances.remove(0);
			ArrayList nuevo=movs(dir);
			System.out.println(nuevo.get(0)+"\t"+nuevo.get(1));
		
		}
		
		
		
	}
	
	/**
	 * 
	 * @param dir	FICHERO A LEER
	 * @return		ARRAYLIST CON 2 SLOTS, MOVIMIENTOS Y MEJOR K
	 * @throws IOException
	 */
	public static ArrayList movs(File dir) throws IOException
	{
		double bestK=0;
		int bestMovs=1000000000;
		double k=0.9;
		int i;
		for (i=0;i<5;i++)
		{
			
	
			//LUEGO DE SELECCIONAR UNA INSTANCIA
			Field field=new Field();
			field.readFile(dir);
			//field.showTime();
					
			//RECORDAR QUE NODE INGRESA CON -1 PORQUE SE SUMA UNO A SU INDICE
			Node solver=new Node(field,-1);
			solver.setMovimientos(solver.solve());
					
			int j;
			int temp;
			//SE INTENTA QUE EL ESTADO INICIAL COMIENZE CON EL NUMERO MENOR POSIBLE
					
			for (j=0;j<5;j++)
			{
				temp=solver.solve();
				if (solver.getMovimientos()>temp)
				{
					solver.setMovimientos(temp);
				}
			}
					
					
			Node aux=solver;
			 j=0;
			while (!aux.complete())
			{
				aux.generarHijos();
				if (aux.getwFlag()==1)
				{	
					if (aux.getMovimientos()+i<bestMovs)
					{
						bestMovs=aux.getMovimientos()+j;
						bestK=k;
					}
					//System.out.println(aux.getMovimientos()+j);
					break;
				}
				aux.filtrarHijos(k);
				aux=aux.roulleteMaker();
				j++;
			}
					
			if (aux.getwFlag()!=1)
			{
				if (aux.getMovimientos()+i<bestMovs)
				{
					bestMovs=aux.getMovimientos()+j;
					bestK=k;
				}
				//System.out.println(aux.getIndice());
				//aux.showtime();
			}
		}
		
		//****************
		
	
		
		//****************
		
		
		ArrayList nuevo=new ArrayList();
		
		nuevo.add(bestMovs);
		nuevo.add(bestK);
		
		return nuevo;
		
	}
	
}
