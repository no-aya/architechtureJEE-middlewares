# Test avec JMeter 
## Confguration du test
### 1- Création d'un plan de test
Après télechargement de Jmeter du [site offciel](https://jmeter.apache.org/download_jmeter.cgi), on ouvre le fichier `JMeter. bat` afin d'utiliser l'interface graphique de l'outil.

On obtient l'interface suivante, on donne un nom à notre test :

![Screenshot 2023-02-19 142335](https://user-images.githubusercontent.com/106016869/219951001-845e9756-42fb-440f-b1a5-e8a98e72fd29.png)

### 2- Création d'un thread group
Pour créer un `Thread Group` : Test Plan (clique droi) > Add > Threads (Users) > Thread Group

![Screenshot 2023-02-19 142802](https://user-images.githubusercontent.com/106016869/219951359-0232dd36-3956-4d00-8478-17b07b1b547d.png)

Nous allons utiliser la configuration suivante :

![Screenshot 2023-02-19 143249](https://user-images.githubusercontent.com/106016869/219951677-6741967f-3f8a-4178-8385-d23b3ee45762.png)

C'est-à-dire 100 utilisateurs se connecteront dans 10 seconds, soit `10 users per sec`.

### 3- Création d'un sampler
Maintenant on ajoute un Sampler à notre test, il s'agira d'une requet `TCP` :

![Screenshot 2023-02-19 143812](https://user-images.githubusercontent.com/106016869/219951836-c421a656-99bb-43cd-b955-fb8e010fbfcb.png)

On précise le serveur (ici c'est `localhost`) et le port de notre serveur. On ajoute aussi un message à envoyer.

![Screenshot 2023-02-19 144019](https://user-images.githubusercontent.com/106016869/219952006-53ba0ab6-41d7-4fc5-9ea2-cdb46610206e.png)

### 4- Création d'un listener
Reste à visualiser les performances de notre test. Nous utiliserons un graph:

![Screenshot 2023-02-19 144300](https://user-images.githubusercontent.com/106016869/219952127-544e04fa-d359-4a1c-8b6c-cefb8f60346a.png)

Les résultats s'afficheront ici sous forme d'un graph.

![Screenshot 2023-02-19 144524](https://user-images.githubusercontent.com/106016869/219952271-93b980ad-a340-47f6-878a-6da153ec49b9.png)

### 5- Exécution du test
https://user-images.githubusercontent.com/106016869/219952826-b9b58e10-392d-4517-8c52-a5295752cf79.mp4

### 6- Résultats
Blocking IO Server Test Results

![Screenshot 2023-02-19 145431](https://user-images.githubusercontent.com/106016869/219952721-91ddaf4a-c099-4031-bf1e-022f9c2b9585.png)

`NOTE : On refait les mêmes étapes pour le serveur Non-Blocking on obtient les résultats suivant`

Non-Blocking IO Server Test Results

![Screenshot 2023-02-19 145904](https://user-images.githubusercontent.com/106016869/219952991-db849aa7-06a5-4df8-a7b4-57a1a93ed439.png)

Pour avoir une idée de la différence entre les deux serveurs, on effectue un test avec un nombre d'utilsateurs qui s'incrémente et on note les résultats. 

| Users | Blocking IO   | Non-Blocking IO |
|-------|---------------|-----------------|
| 10/s  | 330.378 /min  | 376.152 /min    |
| 50/s  | 2354.603 /min | 2324.68 /min    |
| 60/s  | 1820.665 /min | 2858.958 /min   |

## Conclusion
On remarque que le serveur non-blocking est plus performant dès que le nombre d'utilisateurs augmente. 
C'est dû au fait que le serveur non-blocking est capable de gérer plusieurs requêtes en même temps, 
contrairement au serveur blocking qui ne peut gérer qu'une seule requête à la fois.




