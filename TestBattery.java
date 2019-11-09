import MyExceptions.*;
import java.util.*;

public class TestBattery {
	
	public static void RegularTest(SecureDataContainer<MyType> set){
		
		//Creazione utenti
		try {
			set.createUser("Pippo", "00");
			set.createUser("Paperino", "11");
			set.createUser("Topolino", "22");
		} catch (Exception e) { //Stampa qualsiasi eccezione
			System.out.println(e);
		}
		System.out.println("Utenti inseriti: " + set.UsersSize());
		
		//Inserimento dati
		try {
			set.put("Pippo", "00", new MyType("Primo dato"));
			set.put("Paperino", "11", new MyType("Secondo dato"));
			set.put("Pippo", "00", new MyType("Terzo dato"));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//Stampa dati tramite iteratore
		Iterator<MyType> it;
		
		//Il try-catch è stato scritto lungo e poco efficiente di proposito,
		//per consentire una maggiore leggibilità del codice laddòve non ci si aspettano eccezioni.
		try {
			
			//Dati Pippo
			it = set.getIterator("Pippo", "00");
			System.out.println("Dati Pippo: " + set.getSize("Pippo", "00"));
			while (it.hasNext())
				System.out.println(it.next().s);
			
			//Dati Paperino
			it = set.getIterator("Paperino", "11");
			System.out.println("Dati Paperino:" + set.getSize("Paperino", "11"));
			while (it.hasNext())
				System.out.println(it.next().s);
			
			//Dati Topolino
			it = set.getIterator("Topolino", "22");
			System.out.println("Dati Topolino:" + set.getSize("Topolino", "22"));
			while (it.hasNext())
				System.out.println(it.next().s);
			
		}catch (Exception e){System.out.println(e);}
		
		//Condivisione dati
		try {
			set.share("Pippo", "00", "Paperino", new MyType("Terzo dato"));
		} catch (Exception e) {
			System.out.println(e + " in share");
		}
		
		//Test deep copy
		//Aggiungo pluto tra i dati di Pippo, poi provo a modificarlo dall'esterno
		MyType pluto = new MyType("Bau, sono Pluto!");
		try {
			set.put("Pippo", "00", pluto);
			System.out.println("Ultimo dato inserito: " + set.get("Pippo", "00",pluto));
			pluto.s= "Bau bau bau! Ora sono Pluto modificato!";
			System.out.println("Ecco lo stesso dato,\nmodificato da un riferimento esterno: "
					+ set.get("Pippo", "00", new MyType("Bau, sono Pluto!"))
					+ "\n\nNon viene modificato perchè effettua una deep copy!");
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//Modifica dello stesso dato tramite metodo legittimo
		try {
			set.modify("Pippo", "00", new MyType("Bau, sono Pluto!"), pluto);
			System.out.println("\nEcco lo stesso dato, modificato realmente: "
					+ set.get("Pippo", "00", pluto));
		}catch (Exception e) {
			System.out.println(e);
		}
		
		//Visualizzazione dei dati a cui Paperino ha accesso solo in lettura
		Set<MyType> Insieme;
		try {
			Insieme=set.PrintAuthorizedData("Paperino", "11");
			it = Insieme.iterator();
			
			System.out.println("Paperino ha " + Insieme.size() + " dati non personali:");
			while (it.hasNext())
				System.out.println(it.next().s);
		}
		catch (Exception e){
			System.out.println(e);
		}
		
		//Paperino copia il dato nella sua collezione,
		//Pippo lo rimuove dalla collezione originale
		try {
			System.out.println("Adesso Paperino lo copia nella sua collezione...");
			set.copy("Paperino", "11", new MyType("Terzo dato"));
			System.out.println("E Pippo lo rimuove dalla sua...");
			System.out.println(
					(set.remove("Pippo", "00", new MyType("Terzo dato")).toString())
							+ " rimosso!");
			
			//Nuova stampa
			System.out.println("Paperino ha " + set.getSize("Paperino", "11") + " dati:");
			it = set.getIterator("Paperino", "11");
			while (it.hasNext())
				System.out.println(it.next().s);
			
			Insieme=set.PrintAuthorizedData("Paperino", "11");
			it = Insieme.iterator();
			System.out.println("e " + Insieme.size() + " dati non personali:");
			while (it.hasNext())
				System.out.println(it.next().s);
		}
		catch (Exception e){
			System.out.println(e);
		}
		
		//Rimozione utente
		try {
			System.out.println("Rimozione Pippo... ");
			set.RemoveUser("Pippo", "00");
			System.out.println("Numero utenti: " + set.UsersSize());
		} catch (Exception e) {System.out.println(e);}
		
	}
	public static void ExtremeTest(SecureDataContainer<MyCloneable> set){
		
		//Creazione utenti con omonimo
		try {
			set.createUser("Pippo", "00");
			set.createUser("Paperino", "11");
			set.createUser("Pippo", "22");
		}catch (Exception e){
			System.out.println(e);
		}
		
		//Inserimento dato utente non esistente
		try {
			set.put("Topolino", "99", new MyType("Primo dato"));
		}catch (Exception e) {
			System.out.println(e);
		}
		
		//Inserimento dato password errata
		try {
			set.put("Pippo", "16", new MyType("Primo dato"));
		}
		catch (Exception e){
			System.out.println(e);
		}
		
		//Rimozione di un dato non esistente
		try {
			set.remove("Pippo", "00", new MyType("Primo dato"));
		}
		catch (Exception e){
			System.out.println(e);
		}
		
		//Estrazione di un dato a cui non si è autorizzati
		MyType Orazio = new MyType("Primo vero dato");
		try {
			set.put("Pippo", "00", Orazio);
			set.get("Paperino", "11", Orazio);
		}
		catch (Exception e){
			System.out.println(e);
		}
		
		//Condivisione con campo null
		try {
			set.share("Pippo", "00", "Paperino", null);
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
	public static long[] PerformanceTest(SecureDataContainer<MyComplexType> set){
		
		long startTime = System.currentTimeMillis();
		long startMemory = Runtime.getRuntime().totalMemory();
		
		//Inserimento 10k utenti
		for (int i = 0; i < 10000; i++) {
			try {
				set.createUser(("Utente-" + i), "p" + i);
			}catch (Exception e){System.out.println(e);}
		}
		
		//Inserimento di 300000k dati
		for (int i = 0; i <10000 ; i++) {
			
			for (int j = 0; j <30; j++) {
				try {
					set.put("Utente-" + i, "p" + i,
							new MyComplexType(j, Integer.toString(j).charAt(0), j, Integer.toString(j)));
				}
				catch (Exception e) {System.out.println(e);}
			}
			
		}
		
		//Condivisione dati
		for (int i = 0; i <5000 ; i++) {
			
			for (int j = 0; j <15 ; j++) {
				try {
					set.share("Utente-"+i, "p"+i, "Utente-"+(i+750),
							new MyComplexType(j, Integer.toString(j).charAt(0), j, Integer.toString(j)));
				}
				catch (Exception e){System.out.println(e);}
			}
			
		}
		
		//Eliminazione dati
		for (int i = 0; i <1000 ; i++) {
			
			Random r = new Random();
			int k = r.nextInt(30);
			
			try {
				set.remove("Utente-"+i, "p"+i,
						new MyComplexType(k, Integer.toString(k).charAt(0), k, Integer.toString(k)));
			}
			catch (Exception e){System.out.println(e);}
			
		}
		
		//Eliminazione utenti
		for (int i = 0; i <500 ; i++) {
			
			Random r = new Random();
			int k = r.nextInt(10000);
			
			try {
				set.RemoveUser("Utente-" + k, "p" + k);
			}
			catch (UserNotExists u){System.out.println("Utente già eliminato. P = "+i+"/10000");}
			catch (Exception e){System.out.println(e);}
		}
		
		long[] v = new long[2];
		v[0]=System.currentTimeMillis()-startTime;
		v[1]=Runtime.getRuntime().totalMemory()-startMemory;
		return v;
		
	}
	
	public static void main (String args[]){
		/**Prima implementazione.
		 * Casi regolari.
		 **/
		System.out.println("***Casi regolari, prima implementazione***\n");
		
		SecureDataContainer<MyType> set1 = new SecureDataContainer_Impl1<MyType>();
		RegularTest(set1);		
		
		/**Prima implementazione.
		 * Casi limite.
		 **/
		System.out.println("\n\n***Casi limite, prima implementazione***\n");
		
		SecureDataContainer<MyCloneable> set2 = new SecureDataContainer_Impl1<>();
		ExtremeTest(set2);
		
		/**Seconda implementazione.
		 * Casi regolari.
		 **/
		System.out.println("\n\n***Casi regolari, seconda implementazione***\n");
		
		SecureDataContainer<MyType> set3 = new SecureDataContainer_Impl2<>();
		RegularTest(set3);
		
		/**Seconda implementazione.
		 * Casi limite.
		 **/
		System.out.println("\n\n***Casi limite, seconda implementazione***\n");
		
		SecureDataContainer<MyCloneable> set4 = new SecureDataContainer_Impl2<>();
		ExtremeTest(set4);
		
		/**Test su memoria occupata
		 * e velocità.
		 
		System.out.println("\n\n***Test Memoria e velocità:***\n 10k utenti, 300k dati totali" +
				"\n\nPrima implementazione");
		
		SecureDataContainer set5 = new SecureDataContainer_Impl1();
		long[] v = PerformanceTest(set5);
		System.out.println("Secondi: "
						+v[0]/1000+ "\nMemoria occupata: " + v[1]/(1024*1024)+"MB");
		
		System.out.println("\nSeconda implementazione");
		SecureDataContainer set6 = new SecureDataContainer_Impl2();
		v = PerformanceTest(set6);
		System.out.println("Secondi: "
				+v[0]/1000+ "\nMemoria occupata: " + v[1]/(1024*1024)+"MB");
		 **/
		 
	}
}