
# Framework Web en Java

## Auteur 
- Cedric Andriambelo
+ ETU1381

## Présentation du projet
Création d'un framework WEB afin de faciliter et structurer le travail des développeurs Java EE

## Langage utilisé
> Java SE8
> Java EE7

## Structure du code
##### Classes
1. Dossier Framework (sera converti en bibliotheque .jar)
    - package etu1381.framework
        - package annotation
            - Auth
            - OnlyJSON
            - Scope
            - URLAnnotation
        - package file
            - FileUpload
        - package init
            - Infoclass
        - pckage modelview
            - ModelView
        - package servlet
            - FrontServlet
        - package util
            - Utilitaire
        - Mapping

2. Dossier TestFramework (test d'un projet prêt à déployer et utilisant notre bibliotheque framework.jar)
    - package etu1381.framework.model
        - Resetable
        - Departement
        - Employe
        - Ressource
    > Plus queleques views de test

## Description des Sprints de conception
##### Sprint 1 : 
    - mise en place environnement GitHub 
    - création de la classe FrontServlet (sorte de GenericController)

##### Sprint 2 : 
    - création d'une classe Mapping 
        - attribut className (String)
        - attribut method (String)
    - création d'un attribut MappingUrls (dans mon cas "hashmap") de type HashMap<String, Mapping> dans FrontServlet

##### Sprint 3 : mappage (attribution/correspondance) d'une URL avec une méthode des modèles
    - création d'une Annotation URLAnnotation ayant comme cible les methodes et comme valeur une URL
    - création d'une/de plusieurs classes et placement des annotations sur certaines de leurs methodes
    - hydratation de la "hashmap" du FrontServlet dans sa méthode init() (dans mon cas, à l'aide de la classe InfoClass)

##### Sprint 4 : séparation du projet en Framework et TestFramework
    - préparation d'un script.bat pour automatisation la compilation, la création de la librairie et le déploiement local 

##### Sprint 5 : vérification d'une correspondance entre l'URL sur le navigateur et l'une des clés de la hashmap

##### Sprint 6 : envoi de donnees recuperables dans la view (.jsp) cible
    - creation de la classe ModelView
        - attribut data de type HashMap<String, Object>
        - si le type de retour de la methode ciblée est une ModelView, iterer chaque cle et valeur dans le data du ModelView et les mettre dans l'attribut de la requete avant de dispatcher la requete

##### Sprint 7 : récupérer des données depuis un formulaire
    - faire en sorte que les noms des attributs de la classe et des "input" soient les mêmes
    - hydrater un objet avec les attributs récupérés

##### Sprint 8 : remplir les arguments de la methode avec les donnees du formulaire
    - faire en sorte que les noms des "input" soient les mêmes que ceux des arguments

##### Sprint 9 : upload de fichiers 
    - le nom du fichier et son tableau d'octets representatif sont recuperables

##### Sprint 10 : utilisation de singletons (une seule instance de classe)
    - creation d'une annotation Scope afin de definir si une classe devra etre un singleton ou non
    - si oui, c'est cette instance qui sera utilisée dans le traitement
    - sinon, nouvelle instanciation

##### Sprint 11 : authentification des utilisateurs de methodes
    - creation d'une annotation Auth
    - inclure dans web.xml le nom de la variable de session d'authentification pour que le developpeur puisse le personnaliser
    - ajout d'un attribut hashmap sessionToAdd dans ModelView
    - creer une fonction (dans la classe Utilitaire) pour verifier les privileges necessaires et courants pour la methode appelée

##### Sprint 12 : injection des sessions dans le modele pour pouvoir les manipuler
    - creation d'une hashmap sessions dans le modele
    - mettre toutes les variables de sessions dans cet attribut

##### Sprint 13 : conversion du data du ModelView en format JSON
    - creation d'un attribut isJson dans le ModelView
    - verifier si cet attribut est true

##### Sprint 14 : creation de fonctions qui retournent au format JSON (et non ModelView)
    - creation d'une nouvelle annotation
    - type de retour de la methode tel quel et non ModelView

##### Sprint 15 : suppression (dereferencement des variables de session)
    - option 1 : toutes les variables de session (dereferencer la variable HttpSession)
    - option 2 : selectionner les variables de sessions (dans mon cas, checkbox)

## Manuel d'utilisation (pour le developpeur)
> Librairies essentielles : servlet-api.jar, gson-2.10.1.jar
    - s'il veut ajouter des arguments dans les methodes du modele créé, il faudra les mettre en type generiques et non primitives
    - si le developpeur souhaite utiliser l'upload de fichier, il doit specifier le type d'encodage "enctype="multipart/form-data"" sur son formulaire
    - Si le développeur souhaite passer des informations du métier vers la vue, il devra instacier l'attribut data (HashMap) du modele dans la methode utilisee, sinon cet attribut est instanciee par le framework
    - Si le developpeur veut avoir un format JSON, il faudra que sa fonction dans le modele retourne un tableau d'objets
    - Si le developpeur souhaite tester la suppression de plusieurs variables de sessions, il devra mettre le nom de checkbox en "FrameWorksessions"