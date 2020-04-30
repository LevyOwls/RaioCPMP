package clases;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.sound.sampled.Line;

public class Field 
{
	private ArrayList field=new ArrayList();
	private static int hMax=5;
	private static int movs=0;
	private static int columns;
	private static ArrayList movimientos=new ArrayList();
 
	public void setField(ArrayList l)
	{
		field=l;
	}
	//******************COPIA EL CAMPO ACTUAL

	public int getHMax()
	{
		return hMax;
		
	}
	
	public void setHMax(int h)
	{
		hMax=h;
	}
	/**
	 * INGRESA LA LECTURA DE UN ARCHIVO A FIELD
	 * @param f file
	 * @throws FileNotFoundException
	 */
	public void readFile(File f) throws FileNotFoundException
	{
		field.clear();
		int num;
		int columns;
		int containers;
		int q;
		
		 try  (Scanner entrada = new Scanner(f)) 
		    {

		    	//Primero estan las columnas y los contenedores totales.
		        columns = entrada.nextInt();
		        containers=entrada.nextInt();
		        
		       // System.out.println("Entrada: \nColumnas:"+ columnas+" Contenedores: "+contenedores);

		        while (entrada.hasNextInt()) 
		        {
		        	q = entrada.nextInt(); //La cantidad de contenedores de dicha columna
		        	int i;
		        	Column nuevo=new Column();
		        	for (i=0;i<q;i++)
		        	{
		        		num=entrada.nextInt();
		        		nuevo.add(num);
		        		//System.out.print(numero + " "); //se muestra la prioridad de los contenedores de la columna
		        	}
		        	field.add(nuevo);
		        	//System.out.print("\n");
		        	//System.out.println(nuevo);
		         }
		       
		    }
		 
		 
	}
	public  int getMovs() {
		return movs;
	}

	public static void setMovs(int movs) {
		Field.movs = movs;
	}
	/**
	 * IMPRIME EL ESTADO ACTUAL DE FIELD
	 */

	public  void showTime()
	{
		int i;
		Column temp;
		for (i=0;i<field.size();i++)
		{	
			temp=(Column)field.get(i);
			System.out.println(temp+"  "+temp.isLock());
		}
	}
	
	/**
	 * ACTUALIZA EL ESTADO ACTUAL DE LAS COLUMNAS 
	 */
	public  void review()
	{
		int i;
		Column temp;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			temp.generaIdeal();
			temp.setDif();
			temp.setuB();
			temp.setLock();
			
		}
	}
	
	

	
	/**
	 * 
	 * @param cotaSuperior					VARIABLE EXCLUYENNTE, CONSIDERANDO EL DOMINIO DE LOS POSIBLES NUMEROS RANDOM EN [0 ,(N-1)[
	 * @return
	 */
	public  int random(int cotaSuperior)
	{
		int h= (int) Math.floor(Math.random()*cotaSuperior);
		return h;
	}
	
	/**
	 * 
	 * @return ARRAYLIST CON LA PRIORIDAD DE LOS CONTENEDORES EN LA CIMA DE CADA COLUMNA
	 */
	public  ArrayList getTops()
	{
		ArrayList tops=new ArrayList();
		int i;
		int aux;
		Column temp;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			if (!temp.isEmpty())
			{
				aux=(int)temp.get(temp.size()-1);
				tops.add(aux);
			}
			else
			{
				tops.add(0);
			}
		}
		return tops;
	}
	
	
	/**
	 * REVISA LAS COLUMNAS PARA DEFINIR SI ESTA ORDENADO O NO
	 * @return				TRUE EN CASO DE QUE FIELD ESTE ORDDENADO, FALSE EN CASO CONTRARIO
	 */
	public  boolean isOrdenado()
	{
		Column temp;
		int i;
		
		for (i=0;i<field.size();i++)
		{
			
			temp=(Column)field.get(i);
			if (!temp.isLock())
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	
	/**
	 * MUEVE EL ELEMENTO SUPERIOR DE LA COLUMNA ORIGEN A LA COLUMNA DESTINO, NO VERIFICA ALTURA
	 * @param c_o COLUMNA ORIGEN
	 * @param c_d COLUMNA DESTINO
	 */
	public  void atomic_Move(int c_o,int c_d) 
	{
		
		//RECORDAR QUE EL MOVIMIENTO SERA 0 A 1, 2 A 3, 4 A 5 ETC ETC
		
		//SE GENERA UNA LISTA CON LOS MOVIMIENTOS HECHOS
		movimientos.add(c_o);
		movimientos.add(c_d);
		Column temp=(Column)field.get(c_o);
		int aux=(int)temp.quitar();
		temp=(Column)field.get(c_d);
		temp.add(aux);
		movs++;
		review();
		
		
	}	
	
	
	public  int totalContenedores()
	{
		int i;
		int sum=0;
		Column temp;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			sum=sum+temp.size();
		}
		return sum;
	}
	

	
	
	
	
	
	/**
	 * MEDIANTE BUUBLESORT ORDENA COLUMNAS POR TAMAÑO DE MENOR A MAYOR
	 * 
	 * @param size			ARRAYLIST CON TAMAÑO DE CADA COLUMNA
	 * @param columna		ARRAYLIST CON EL NUMERO DE COLUMNA
	 * @return				ARRAYLIST CON NUMERO DE COLUMNAS ORDENADA DE MENOR A MAYOR
	 */
	public  ArrayList bubbleSort(ArrayList size,ArrayList columna)
	{
		 boolean sorted=false;
		 int aux,i;
		 while (!sorted)
		 {
			 sorted = true;
			 for (i=0;i<size.size()-1;i++)
			 {
				 //SI I>I+1 CAMBIAN DE POSICION
				 if ((int)size.get(i)>(int)size.get(i+1))
				 {
					 //SE ORDENA POR TAMAÑO
					 aux=(int)size.get(i);
					 size.set(i,size.get(i + 1));
					 size.set(i + 1,aux);
					 //SE ORDENA POR COLUMNA
					 aux=(int)columna.get(i);
					 columna.set(i,columna.get(i + 1));
					 columna.set(i + 1,aux);
					 
					 sorted=false;
				 }
			 }
		 }
		 
		 return columna;
	}
	

	
	

	

	
	
	public static int dif(int n1,int n2)
	{
		if (n1>=n2)
		{
			return n1-n2;
		}
		
		return n2-n1;
		
	}

	
	
	
	
	
	/***************************************************************/
	/**************************RANDOM FUNCTIONS*********************/
	
	
	public  void writeBase(String s) throws IOException
	{
		FileWriter file = new FileWriter(s);
		PrintWriter pw = new PrintWriter(file);
		/*
		 * FILAS	COLUMNAS
		 * 
		 * TAMAÑO_COLUMNA N N1 N2 N3......
		 * TAMAÑO_COLUMNA N N1 N2 N3......
		 */
		
		pw.println(field.size()+" "+totalContenedores());
		
		int i,j;
		Column temp;
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//CANTIDAD DE ELEMENTOS EN LA COLUMNA
			pw.print(temp.size()+" ");
			
			//ELEMENTOS EN LA COLUMNA
			for (j=0;j<temp.size();j++)
			{
				pw.print(temp.get(j)+" ");
			}
			pw.println();
		}
		
		file.close();
	}
	/**************************************************/
	//FUNCIONES CUANDO EL ALGORITMO INICIA
	/**************************************************/	

	/**
	 * 
	 * @return				RETORNA LA QUE MAS CONTENEDORES PUEDA TENER (NORMALMENTE, NUMERO MAS GRANDE)
	 */
	public int analyzer()
	{
		ArrayList columnas=new ArrayList();
		ArrayList contenedoresPosibles=new ArrayList();
		Column temp;
		int i;
		
		int top;
		for (i=0;i<field.size();i++)
		{
			//SE OBTIENE LA COLUMNA
			temp=(Column)field.get(i);
			

			//SI LA COLUMNA ESTA ORDENADA & ES MENOR A HMAX & SU SIZE ES DIFERENTE DE 0
			if (temp.isLock() && temp.size()<hMax && temp.size()!=0) 
			{
				columnas.add(i);
				top=(int)temp.get(temp.size()-1);
				contenedoresPosibles.add(subAnalyzer(top, i));
			}	
		}
		
		//SE ORDENA DE MENOR A MAYOR
		columnas=bubbleSort(contenedoresPosibles, columnas);
		
		if (columnas.size()==0)
		{
			return -1;
		}
		int mayor=(int)columnas.get(columnas.size()-1);
		return mayor;
	}
	
	/**
	 * 
	 * @param n						contenedor en la cima
	 * @param c_o					columna origen
	 * @return
	 */
	public int subAnalyzer(int n,int c_o)
	{
		Column temp;
		int i;
		int top;
		int tempSize;
		int contador=0;
		for (i=0;i<field.size();i++)
		{
			//SE OBTIENE LA COLUMNA
			temp=(Column)field.get(i);
			
			//SI NO ESTA VACIA, NI ESTA ORDENADA, NI ES C_O
			if (!temp.isEmpty() && !temp.isLock() && i!=c_o)
			{
				tempSize=temp.size()-1;
				
				//SI EL TOP MENOR ES MENOR O IGUAL QUE N
				top=(int)temp.get(tempSize);
				
				if (top<=n)
				{
					contador++;
					//Y SE PROCEDE A REVISAR EL RESTO
					tempSize--;
					
					//-1, CUANDO LLEGA A 0 TERMINA DE RECORRER EL ARRAYLIST
					while ((int)temp.get(tempSize)<=top)
					{
						top=(int)temp.get(tempSize);
						tempSize--;
						contador++;
						if (tempSize==-1)
						{
							break;
						}
					}
				}
				
			}
			
			
		}
		return contador;
	}
	
	
	public int bigFill()
	{
		Column temp;
		int i;
		
		ArrayList columnas=new ArrayList();
		ArrayList size=new ArrayList();
		
		for (i=0;i<field.size();i++)
		{
			//SE OBTIENE LA COLUMNA
			temp=(Column)field.get(i);
			
			//SI ESTA ORDENADA Y ES MENOR A HMAX
			if (temp.size()<hMax && temp.isLock())
			{
				columnas.add(i);
				size.add(temp.size());
			}
			
		}
		
		columnas=bubbleSort(size, columnas);
		
		//UNA VEZ ENCONTRADAS LAS COLUMNAS MAS ALTAS, SE AGREGAN A UN ARRAYLIST
		
		temp=(Column)field.get((int)columnas.get(columnas.size()-1));
		int big=temp.size();
		
		ArrayList columnasFiltradas=new ArrayList();
		
		for (i=0;i<field.size();i++)
		{
			//SE OBTIENE LA COLUMNA
			temp=(Column)field.get(i);
			//SI ESTA ORDENADA Y ES MENOR A HMAX
			if (temp.size()==big && temp.isLock())
			{
				columnasFiltradas.add(i);
			}	
		}
		
		
		columnas=columnasFiltradas;
		
		//NO ENCONTRO NINGUNA COLUMNA
		if (columnas.size()==0)
		{
			return -1;
		}
		if (columnas.size()==1)
		{
			return (int)columnas.get(0);
		}
		//AHORA QUE SE TIENEN LAS COLUMNAS MAS GRANDES, SE VE CUAL ES EL MEJOR RELLENO PARA ELLAS
		
		int top;
		int cP;
		ArrayList posibles=new ArrayList();
		for (i=0;i<columnas.size();i++)
		{
			//OBTIENE LA COLUMNA
			temp=(Column)field.get((int)columnas.get(i));
			
			if(temp.size()!=0)
			{
				//SE OBTIENE EL TOP
				top=(int)temp.get(temp.size()-1);
				
				cP=subAnalyzer(top,(int)columnas.get(i));
				
				posibles.add(cP);
			}
			
		}
		
		
		//SE BUSCA EL QUE TENGA MAS POSIBILIDADES
		
		//OJO, TEORICAMENTE EL QUE TENGA MAS CONTENEDORES POSIBLES ES EL QUE TIENE EL NUMERO DE CONTENEDOR MAS ALTO
		
		columnas=bubbleSort(posibles, columnas);
		
		int column=(int)columnas.get(columnas.size()-1);
		temp=(Column)field.get(column);
		if (temp.size()==0)
		{
			return -1;
		}
		top=(int)temp.get(temp.size()-1);
		if (subAnalyzer(top, column)==0)
		{
			return -1;
		}
		
	
			return column;
		
	}
	
	
	public int bestMove(int c_d)
	{
		if (c_d==-1)
		{
			return -1;
		}
		Column temp=(Column)field.get(c_d);
		int top= (int)temp.get(temp.size()-1);
		int i;
		int menor=-1;
		int columnaMenor=-1;
		
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			
			//SI NO ES COLUMNA ORIGEN, NI ESTA ORDENADA NI ESTA VACIA
			if (i!=c_d && !temp.isLock() && !temp.isEmpty())
			{
				//SI MENOR<NUMERO<=TOP
				if (menor<(int)temp.get(temp.size()-1) && (int)temp.get(temp.size()-1)<=top)
				{
					menor=(int)temp.get(temp.size()-1);
					columnaMenor=i;
				}
			}
		}
		
		if (menor==-1)
		{
			return menor;
		}
		
		//LUEGO DE OBTENER EL MENOR SE REVISA SI EXISTE EN OTRAS COLUMNAS.
		
		ArrayList select=new ArrayList();
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//SI NO ES COLUMNA ORIGEN, NI ESTA ORDENADA NI ESTA VACIA
			if (i!=c_d && !temp.isLock() && !temp.isEmpty())
			{
				if ((int)temp.get(temp.size()-1)==menor)
				{
					select.add(i);
				}
			}
		}
		//UNA VEZ OBTENIDAS LAS SIGUIENTES, SE VE LA MAS CONVENIENTE A RETIRAR
		int menorD=0;
		if (select.size()==1)
		{
			return (int)select.get(0);
		}
		else
		{
			
			for (i=0;i<select.size();i++)
			{
				temp=(Column)field.get((int)select.get(i));
				//CONSIDERANDO EL HECHO DE QUE SI NO ESTA ORDENADA AL MENOS DEBEN EXISTIR 2 CONTENEDORES
				//SE PUEDE ACCEDER A SIZE-2
				//SI MENOR<NUMERO<=TOP
				if (menorD<(int)temp.get(temp.size()-2) && (int)temp.get(temp.size()-2)<=menor)
				{
					menorD=(int)temp.get(temp.size()-2);
				}
				
			}
		}
		
		for (i=0;i<select.size();i++)
		{
			temp=(Column)field.get((int)select.get(i));
			if ((int)temp.get(temp.size()-2)==menorD)
			{
				return (int)select.get(i);
			}
			
		}
		//CUANDO QUEDAN SOLO COLUMNAS DE SIZE 1 PARA COLOCAR COSAS, LLEGA A 22 Y AHI SE CAMBIA LA ESTRATEGIA.
		return 22;
	}
	
	/**************************************************/
	//FUNCIONES POST BIG FILL
	/**************************************************/
	public int bestMoveNo22(int c_d)
	{
		Column temp=(Column)field.get(c_d);
		int top= (int)temp.get(temp.size()-1);
		int i;
		int menor=-1;
		int columnaMenor=-1;
		
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			
			//SI NO ES COLUMNA ORIGEN, NI ESTA ORDENADA NI ESTA VACIA
			if (i!=c_d && !temp.isLock() && !temp.isEmpty())
			{
				//SI MENOR<NUMERO<=TOP
				if (menor<(int)temp.get(temp.size()-1) && (int)temp.get(temp.size()-1)<=top)
				{
					menor=(int)temp.get(temp.size()-1);
					columnaMenor=i;
				}
			}
		}
		
		if (menor==-1)
		{
			return menor;
		}
		
		//LUEGO DE OBTENER EL MENOR SE REVISA SI EXISTE EN OTRAS COLUMNAS.
		
		ArrayList select=new ArrayList();
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//SI NO ES COLUMNA ORIGEN, NI ESTA ORDENADA NI ESTA VACIA
			if (i!=c_d && !temp.isLock() && !temp.isEmpty())
			{
				if ((int)temp.get(temp.size()-1)==menor)
				{
					select.add(i);
				}
			}
		}
		
		int ret=this.random(select.size());
		
		return (int)select.get(ret);
		
	}
	/**************************************************/
	//ZERO FUNCTIONS (INCLUYE LOS VACIOS, LUEGO DE SACAR TODO LO QUE SE PODIA SACAR DE FORMA NORMAL)
	/**************************************************/
	
	/**
	 * 
	 * @return			PRIMERA UBICACION DE UN SLOT VACIO, SI NO HAY RETORNA -1
	 */
	public int firstEmpty()
	{
		int i;
		Column temp;
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			if (temp.size()==0)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public int bestMoveZero(int c_d)
	{
		int i;
		Column temp;
		int mayor=0;
		int mayorC=-1;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//SI NO ESTA ORDENADO Y LA COLUMNA NO ESTA VACIA Y NO ES LA COLUMNA DESTINO
			if (!temp.isLock() && !temp.isEmpty() && i!=c_d)
			{
				if ((int)temp.get(temp.size()-1)>mayor)
				{
					mayor=(int)temp.get(temp.size()-1);
					mayorC=i;
				}
			}
		}
		
		//SE BUSCA SI HAY MAS DE UN MAYOR

		ArrayList select=new ArrayList();
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//SI NO ES COLUMNA ORIGEN, NI ESTA ORDENADA NI ESTA VACIA
			if (i!=c_d && !temp.isLock() && !temp.isEmpty())
			{
				if ((int)temp.get(temp.size()-1)==mayor)
				{
					select.add(i);
				}
			}
		}
		
		int mayorD=0;
		//SE REVISA LA MAS CONVENIENTE
		if (select.size()==1)
		{
			return (int)select.get(0);
		}
		else
		{
			
			for (i=0;i<select.size();i++)
			{
				temp=(Column)field.get((int)select.get(i));
				//CONSIDERANDO EL HECHO DE QUE SI NO ESTA ORDENADA AL MENOS DEBEN EXISTIR 2 CONTENEDORES
				//SE PUEDE ACCEDER A SIZE-2
				//SI MENOR<NUMERO<=TOP
				if (mayorD<(int)temp.get(temp.size()-2) && (int)temp.get(temp.size()-2)<=mayor)
				{
					mayorD=(int)temp.get(temp.size()-2);
				}
				
			}
		}
		
		for (i=0;i<select.size();i++)
		{
			temp=(Column)field.get((int)select.get(i));
			if ((int)temp.get(temp.size()-2)==mayorD)
			{
				return (int)select.get(i);
			}
			
		}
		//CUANDO QUEDAN SOLO COLUMNAS DE SIZE 1 PARA COLOCAR COSAS, LLEGA A 22 Y AHI SE CAMBIA LA ESTRATEGIA.
		return 22;
		
	}
	
	
	public int bestMoveZeroNo22(int c_d)
	{
		int i;
		Column temp;
		int mayor=0;
		int mayorC=-1;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//SI NO ESTA ORDENADO Y LA COLUMNA NO ESTA VACIA Y NO ES LA COLUMNA DESTINO
			if (!temp.isLock() && !temp.isEmpty() && i!=c_d)
			{
				if ((int)temp.get(temp.size()-1)>mayor)
				{
					mayor=(int)temp.get(temp.size()-1);
					mayorC=i;
				}
			}
		}
		
		return mayorC;
	}
	
	/**************************************************/
	//SACRIFICIO
	/**************************************************/
	
	public void AIOVacateColumn()
	{
		Column temp;
		int i;

		//int col=selectColToVacate();
		
		//int col=select_column();**
		
		int col=selectMinumCol();
		temp=(Column)field.get(col);
		int selector;
		
		while (temp.size()!=0)
		{
			
			//PRIMERO SE VE SI PUEDE UBICARSE EN OTRO LUGAR AHORRANDO MOVIMIENTOS
			selector=posibleMove((int)temp.get(temp.size()-1),col);
			if (selector!=-1)
			{
				atomic_Move(col,selector);
				
				if(temp.isLock() && !temp.isEmpty())
				{
					if (Contador(col)+temp.size()>=hMax)
					{
						break;
					}
				}
			}
			else
			{
				//EN CASO DE QUE NO PUEDA ENVIARSE A UN LUGAR DONDE SE AHORRE MOVIMIENTO SE MUEVE A UN LUGAR CONVENIENTE
				selector=bestVacateC_d(col);
				if (selector!=-1)
				{
					atomic_Move(col,selector);
					
					if(temp.isLock() && !temp.isEmpty())
					{
						if (Contador(col)+temp.size()>=hMax)
						{
							break;
						}
					}
				}
				//SI NO PUEDE ENVIARSE A UN LUGAR CONVENIENTE, SE REALIZA UN MOVIMIENTO ERRONEO A PROPOSITO
				else
				{
					selector=badMove(col);
					
					atomic_Move(col,selector);
					
					if(temp.isLock() && !temp.isEmpty())
					{
						if (Contador(col)+temp.size()>=hMax)
						{
							break;
						}
					}
					
				}
			}
		}
		
	}
	
	public int selectMinumCol()
	{
		ArrayList size=new ArrayList();
		ArrayList columna=new ArrayList();
		int i;
		int aux;
		Column temp;
		//SE LEE EL TAMAÑO Y LA COLUMNA 
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			size.add(temp.size());
			columna.add(i);
		}
		
		//SE ORDENA DE MAYOR A MENOR, CON UN COLUMNSORT PARA REPETIR LOS PASOS EN AMBAS LISTAS
		
		ArrayList col=bubbleSort(size, columna);
		 
		columna=col;
		
		return (int)columna.get(0);
		
		
	}
	
	/**
	 * REVISA SI PUEDE COLOCARSE SOBRE ALGUNA COLUMNA
	 * @return 		NUMERO DE COLUMNA EN LA CUAL PUEDE POSICIONARSE O -1 EN CASO DE NO ENCONTRAR NINGUNA
	 */
	public  int posibleMove(int n,int c_o)
	{
		ArrayList tops=getTops();
		int i;
		int aux;
		int dif=n;
		int col=-1;
		Column temp;
		ArrayList desordenados=disorderlyElements();
		int bigDis=(int)desordenados.get(desordenados.size()-1);
		for (i=0;i<tops.size();i++)
		{
			aux=(int)tops.get(i);
			if (n<=aux)
			{
				if (dif>aux-n)
				{
					temp=(Column)field.get(i);
					//QUE NO SEA LA COLUMNA ORIGEN, QUE TENGA ESPACIO DISPONIBLE Y QUE ESTE ORDENADA
					if (temp.size()<hMax && i!=c_o && temp.isLock())
					{
						col=i;
						break;
					}
					
				}
			}
			//COLUMNA VACIA
			else
			{
				if (aux==0 && n==bigDis)
				{
					col=i;
					break;
				}
			}
			
			
		}
		return col;
	}
	/**
	 * GENERA UNA LISTA CON LA PRIORIDAD DE LOS CONTENEDORES DESORDENADOS, SIN REPETIR PRIORIDADES.
	 * @return lista con prioridades de contenedores desordenados.
	 */
	public  ArrayList disorderlyElements()
	{
		ArrayList elements=new ArrayList();
		
		Column temp;
		
		int i;
		int j;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//SI LA COLUMNA NO ESTA ORDENADA
			if (!temp.isLock())
			{
				for (j=0;j<temp.size();j++)
				{
					elements.add(temp.get(j));
				}
			}
		}
		Collections.sort(elements);
		
		ArrayList unrepeated=new ArrayList();
		int helper;
		for (i=0;i<elements.size();i++)
		{
			helper=(int)elements.get(i);
			if (!unrepeated.contains(helper))
			{
				unrepeated.add(helper);
			}
		}
		
		return unrepeated;
	}
	
	/**
	 * BUSCA LA CANTIDAD DE CONTENEDORES QUE SE LE PUEDEN SOBREPONER CON LOS TOPS ACTUALES
	 * @param c_o				COLUMNA ORIGEN
	 * @return					CANTIDAD DE CONTENEDORES DISPONIBLES PARA SOBREPONERSE
	 */
	public int Contador(int c_o)
	{
		int cont=0;
		int i,aux;
		Column temp=(Column)field.get(c_o);
		
		//NUMERO AL CUAL SE LE QUIERE BUSCAR LA CANTIDAD DE CONTENEDORES QUE SE PUEDE SOBREPONER
		
		
		int top=(int)temp.get(temp.size()-1);
		
		for (i=0;i<field.size();i++)
		{
			//COLUMNA ACTUAL
			temp=(Column)field.get(i);
			
			//SI LA COLUMNA ACTUAL ESTA DESORDENADA, NO ES LA COLUMNA ORIGEN Y NO ESTA VACIA
			if (!temp.isLock() && i!=c_o && !temp.isEmpty())
			{
				//AUX TOMA EL VALOR DE EL CONTENEDOR TOP 
				aux=(int)temp.get(temp.size()-1);
			
				//EN CASO DE QUE SEA MENOR AL TOP DE C_O, SE CONSIDERA UN CANDIDATO DISPONIBLE
				if (aux<=top)
				{
					cont++;
				}
			}
		}
		
		return cont;
	}
	
	
	/**
	 * RETORNA LA MEJOR COLUMNA DESTINO PARA APOYAR UN CONTENEDOR, ESTA SE EVALUA OBSERVANDO QUE SEA MENOR AL CONTENEDOR QUE SE LE QUIERE PONER ENCIMA Y LA DIFERENCIA SEA INFIMA
	 * @param c_o 		COLUMNA ORIGEN
	 * @return 			MEJOR UBICACION POSIBLE PARA RETIRAR EL CONTENEDOR DE LA COLUMNA
	 */
	public int bestVacateC_d(int c_o)
	{
		
		int i;
		ArrayList tops=getTops();
		Column temp;
		int coTop=(int)tops.get(c_o);
		int col=-1;
		int dif=200;
		int aux;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			
			if (i!=c_o && temp.size()<hMax)
			{
				aux=(int)tops.get(i);
				if (coTop>=aux)
				{
					if ((coTop-aux)<dif)
					{
						col=i;
					}
				}
				
			}
		}
		
		
		return col;
	}
	
	
	public int badMove(int c_o)
	{
		int i;
		Column temp=(Column)field.get(c_o);
		int menorDif=100000;
		int dif,aux;
		int col=-1;
		int top=(int)temp.get(temp.size()-1);
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			
			if (i!=c_o && temp.size()<hMax)
			{
				aux=(int)temp.get(temp.size()-1);
				if (dif(aux,top)<menorDif)
				{
					menorDif=dif(aux,top);
					col=i;
				}
			}
		}
		return col;
	
	}
	

}
	
	
	
	

	
	
/*
 * 
 * 
	                                                                           `-:-.`                                                                                                                  
                                                                         .:++++++/-`                                                                                                               
                                                                       ./+++/:-:/+o+:`                                                                                                             
                                                                     `:+o+/:-----::+oo-`                                                                                                           
                                                                   `:+oo+:---------:/+o+-                                                                                                          
                                                                  -+oo+/-------::::--:/oo/.         -/+/:.                                                                                         
                                           `..`                 `:ooo+:::::--:/+++++/--:+oo:`     ./o+/+++:`                                                                                       
                                         -/+++o++:.`           ./ooo+:----::-/+++/.-+/---:+o+-`  .os/:::/+++-                                                                                      
                                        :+++///+++o+/:.`      .+ooo+::------:++++/``:/----:/+o/`.+o/::::::/++:`                                                                                    
                                       :+o+/::::::///+++:-`  ./+oo+:---------/+++++/+/------:+o++o/::::::::/++-                                                                                    
                                      .++++:::::::::--://+/:-/++++:----------:+++++++:-------:+oo+:--:::::::/++-                                                                                   
                                      :oo+/::::::::::---:::/++++/:------------:/+++/:---------:++/----:::::::+++.                                                                                  
                                     `+oo+/::::::::::-------://::---------------:::------------::-----:::::::/++/`                                                                                 
                                     -ooo+/:::::::::::---------------------------::////////::----------:::::::+o+.                                                                                 
                                     -ooo+/:::::::::::----------------------:/osyhhhhhhhhhhhhyys+/:----:::::::/oo/                                                                                 
                                     :oooo/::::::::::::::/++++o+++///:--:/+yhhhhyso/::----:/+osyyhyo/:--::/:::/oso`                                                                                
                                     :oooo/::::::::/+oyyhhhdddddddhhhysoshdhy+:.```           ``.:oyhs+ssyyyyysyhs-`                                                                               
                                     .osso/::::::+shhdhhso/::-----:/+syhdhs:``                    ``--////////+syhhyo/`                                                                            
                                     `osss+::::+yhdhy+-.``          ```-/:.                   ```..-----.````````.-+shyo-                                                                          
                                     `/ssss:::+hddy:``   `.--:::::-..````````````````````` ``.:oyyhhhhhhhyo+:.```````-oyho-                                                                        
                                      -syyysyyhddy-   `-+syhhhhhhhhhyso/:.`````````````````-+yhdhysoooosyhddhhs+-.`````.ohh:                                                                       
                                   ./syhhys+//+yy+.`-+yhhhhddddddddddhdhhyo/-.```````````.+ydhs+:--------:/oyhdhhs+-.````:yh+`               :.                                                    
                                 .+hhhyo:.`````....+yhddddhysooooosyyhhddddhyo:.`````````+hho/--------------:/oyhddyo:.```-yh/              `s-                                                    
                                /ydhs:.``````````:shddddhs+:---------/+syhddddhy/:::::::/oso+////:-------------:/oyddhs:.``:hh.              .`                                                    
                               +hdh/.` ````````./hddddhy+:--------------:/oyhdddhhhhhhhhhhhhhhhhhhhys/--------:::::+yhdhs:.-yd-                                                                    
                              `ydd+` `````````-ohddddhs:::-:----------------/oyyyyssooo++/////////+o+:-----::::::::::+yhdho:yh.                                                                    
                              `ohy:``...`````.sdddddhs::::::::-----------------------------------------:--:::::::::::::ohddhho                                                                     
                               .+sssyyyys+/.`-hdddddy/:::::::::://:------------------------------------:::::::::::+/::::/shdds`                                                                    
                              `+hddddddddhy/-:oyyyyo/::::::::::+hho:------------------------------------:::::::::shh+::::/ohdds-                                                                   
                              -hdddhysoooo+:::::///////::::::::sddy/:------------------------------------:-::::::yddo::::+syhhdh/               `:`                                                
                              :hdddho//////:::/+ossssssso::::::oddh/::-----------------/o/----------:oo:-::::::::+yy+::::+sssyhdh+.              `.                                                
                              -yddddy//////:::+ssssssssss/::::::+o+:::::::-------------/sy/---------:o+::-::::::::::::::://ossyyhhs-                                                               
                           `-/shddddy///////::/+ossooo+//:::::::::::::::::::------------::--------------::::::::::::::::::///+++sddy-                                                              
                          -ohdddddhyo///////::::::::::::::::::::::::::::::::::::-----------------------::::::::::::::::::////////shdy-                                                             
                         `shddddhs+//////////::::::::::::---::::::::::::::::-::----:::://+++oooossoo+//::::::::::::::::::////////+hdh+                                                             
                         .yhddddhs////////////::::::::::::::::::::::::::::::://+oossyyhhhhhhhyyssssyhhhyyso+/:::::::::::::://////+hdh:                                                             
                         `shdddddhs///////////::::::::::::-::::::::///+ossyyyyyyyysso+/::----...``..--:/osyyyyyso+//::::::://////shdy.                                                             
                          -yddddddho//////////::::::::::::::/++osyyhhhyyso++/:--...``````````````````````..--/+osyhhyo::::://///ohdd+                                                              
                           -yddddhs//////////:::::://++ossyyyyyso+/::-....`````````````````````````````````````...-:++-..-::://ohddy`                                                              
                            ./+o++/////::::::::::::+ssso+//:---......`````````````````````````````````````````````````````..-:ohddh-                                                               
                                 -oss+:------........................................```````````````````````````````````````.+hddy:                                                                
                                 /hddh/-..................................................`````````````````````````````````-shdhy-                     ``                                          
                                -ydddy-.........................................................`````````````````````````./yhdhs-                                                                  
                               `shddd+........................................................................`````````.:shddh/`                                                                   
                               `hddddyo/////::--................................................................`````.:shddhs-                                                                     
                              `-sddddddddddddhhyso+:--..........................................................``../shddhy/`                                                                      
                            .+shdddhhyysssyyyyhhhdddys/--........................................................-+yhdddy/.                                                                        
                          .ohddhs+:.```` `````.--/+syhdho//++oo+/:--:////::--........................----..:/ossyhhhhddh+-`                                                                        
                         -hddy/.`````````````      ``-/sys++/++osyyyyysssyyyys+/-.................-/oyyysoshhs+/----::+oyhy+-`     `...                                                            
                         oddh-```````````````````      `.`     ``./:.`````.-:+syy+-...---....--:::syo/-...-:-`        ``.:+yhs-  `:shhys/`                                                         
                         oddh:.`````````````````````                    `     `.+yy:/+ooo/:-/syyyys:`            ``````````-+yh/`+hdo/odds.                                                        
                         :ddds-.-://++oo+:.````````````````    ``````````````````/hhhhyyyssyhy/....````      `````````.::..../hhsddo---sdds`                                                       
                        ``/ysoshhddddddddhs/.```````````````````````````````````.shhso++/..+s-````````````````````````-shsyyyhdddho:---+hdh/                                                       
                      .oysosyddddhhhyyhhddddyo/-..``````````````.-:-..`````````./hdyo++++++++/.`````````````````````.:ydyyssoooo+/--:::/hddo`                                                      
                     :yddysshdddy/:::::/shhddddhysoo++////+/::/+shdhyso+/:::::/ohddho+++++++yds-.......-//-.`````..:ohho------:::::::::+hddo`                                                      
                    /hhhyssshdddh/::::::::/+oyhhhdddddddddddhhyo+/osyhhhhhhhhhhysyddhyssossydddhhyyyyyhhhdhs+///+syhyo:----::://////:/+hddh:                                                       
                   :hhhyssssydddds::::::---.`.-://+oossssoo+/-..```..-::////::-..-+yhhdhhhhyo/++ooooo++/:+yhhhhyys+:-.--:::///+++oosyhdddy:                                                        
                  :yhhhsssssshdddh+::::::--``````````````````````.`.........`````..-::///:-.``````````````..--...`````-/syyhhhhddddddddh+.                                                         
                 -yhhhyssssssyddddo::::::-.``````````.``````..------------------/syyyo/-.`````````````````````````````:hddddddddddddddy`                                                           
                 shhhyssssyyhddddy/::::::-.`````````````````.-::::::------------shhhddddy/.```````````````````````````./hddddhhhyyyyhhy.                                                           
               `/hhhysssyhdddddy+/::::::-.`````````````````.-::::::::::::::::-----::/+yddds-```````````````````````````.odddhdhhyssssyyo.                                                          
               -yhhhssshddddhs+//:::::::-``````...````````.+yhyo/::::::::::::::::::::-:+ddds````````````````````````````-yddyoyddhsssyyy/`                                                         
              `ohhhysshdddhs+///::::::::.`````........````.odddddhyso+++//////:::::::/+ydddy````````````````````````````./hdds:oddysssyyy-                                                         
              :yhhyssydddds////::::::::-.```............```.+hdddddddddddddhhhysssyyhdddddd/```````````````````````......-oddh+ydhsssssyyo`                                                        
              +hhhyssyddddy+///::::::::-.``..............````.:+oyhdddddddddddddddddddhyosdy.````````````````````.........:hddhdhssssssyyy:                                                        
              +hhyssssyddddy+//::::::::-``................````````.-:/yddyyyyyyssoo+/::--/hd+```````````````````......-----oddddsssssssyyyo`                                                       
              +hhysssssyddddds+::::::::..........-..........````.````.odho++///:::::://+osddo.``````````````````...--------:hddddyssssssyyy:                                                       
              +hhsssssssyhdddds:::::::-.........--................```.+dds+ossssosssssyyhhdy-``````````````````...----------yddhhhysssssyyy+`                                                      
              +hysssssssssydddy/::::::-.....--........................-sdddhhhy+/shdddddhy+-..........```.........-.--------sddyohdhsssssyyy.                                                      
              +hsssssssssydddds/::::::-.......-.........................:+syyddo:/syds+:--......................------------sddy/ohdyssssyyy/                                                      
              +yssssssssyddddy+/:::::::-.......-............................-hdy:--yd+..............................-------/yddyshdhssssssyyo`                                                     
              +ysssssssshddddo//:::::---.........-..........................-hdy:--yd+.............................---..--:ydddddhysssssssyys.                                                     
              +ssssssssyddddh+///:::::--.........-..........................:hdy:--ydo...............................----+yddddhhsssssssssyyy-                                                     
              /ssssssssyhddddyo++/:::::-..................................../ddh++ohds...............................-:+yhddhhyyssssssssssyyy/                                                     
              /sssssssssyddddddddhy+::::-........-............-....--......./ddho++sdy..--.....-...................-/ohddddhysssssssssssssyyy+      
 */
	
	

