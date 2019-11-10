# Secure-Data-Container
###### Progetto universitario per il corso di Programmazione II - Università di Pisa, a.a. 2018/2019

Il progetto ha l’obiettivo di applicare i concetti e le tecniche di programmazione (Object-Oriented, Defensive programming, Design by contract) esaminate durante il corso. 
Lo scopo del progetto è lo sviluppo di un componente software di supporto alla gestione di insiemi di dati.
Si richiede di progettare, realizzare e documentare la collezione SecureDataContainer<E>.
SecureDataContainer<E> è un contenitore di oggetti di tipo E. 
Intuitivamente la collezione si comporta come un Data Storage per la memorizzazione e condivisione di dati
(rappresentati nella simulazione da oggetti di tipo E).
  
La collezione deve garantire un meccanismo di sicurezza dei dati fornendo un proprio meccanismo di gestione delle identità 
degli utenti. 
Inoltre, la collezione deve fornire un meccanismo di controllo degli accessi che permette al proprietario del dato di eseguire
una restrizione selettiva dell'accesso ai suoi dati inseriti nella collezione. 
Alcuni utenti possono essere autorizzati dal proprietario ad accedere ai dati, 
mentre altri non possono accedervi senza autorizzazione.
La collezione prevede l'utilizzo di un meccanismo di cifratura per i dati presenti all’interno della stessa.

Il progetto prevede la specifica completa dell'interfaccia java del tipo di dato SecureDataContainer<E> e due diverse implementazioni, basate su differenti strutture di supporto.
Per entrambe le implementazioni sono richieste funzione d'astrazione ed invariante di rappresentazione.

Per valutare il comportamento dell’implementazioni proposte è stata realizzata una batteria di test in grado di
operare, senza modifiche specifiche, su entrambe le implementazioni proposte.
#### Nota: L'intero progetto è stato sviluppato e distribuito riservando particolare interesse al tempo di consegna.
