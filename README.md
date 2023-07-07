# Note : les parametres des methodes dans les classes doivent etre de types generiques
# Note : le developpeur doit mettre en attribut la description du fichier a uploader (ex : pdp, CIN,...)
# lorsque le developpeur souhaite implementer l'upload de fichiers, il doit specifier le type d'encodage "enctype="multipart/form-data"" sur son formulaire
# Si le développeur souhaite passer des informations du métier vers la vue, il devra instacier l'attribut data (HashMap) du modele dans la methode utilisee, sinon cet attribut est instanciee par le framework
# Si le developpeur veut avoir un format JSON, il faudra que sa fonction dans le modele retourne un tableau d'objets