FAILLE: 
    TYPE: Injection JS/XSS
    ACTION : Diverse
    DESCRIPTION: Injection de scripts malveillants JavaScript ou XSS dans la base de donné pour l'exécuté dans une page html 

    
EXPLOIT: 
    1. Création d'un compte nommé Vilain avec comme mot de passe 12345
    2. En étant connecté avec le compte Vilain, cliquer sur le bouton NOUVELLE TÂCHE pour aller à la page de création de tâche
    3. Sur la page création de Tâche, entrer <script>alert("Gros Méchant code!")</script> dans la barre nom et entré la date du 31 mai 2021 (Exemple, doit être après la date du jour).
    4. À l'aide d'un navigateur, entrer l'adresse https://rhubarb-cobbler-43725.herokuapp.com/index pour avoir accès à la page index.
    5. Admirer le message Gros Méchant code qui apparait à l'écran.
    
On a pu, à partir du compte Vilain, envoyer une injection à notre serveur et qui s'exécute au moment où on fait appel à la page html index contenant le nom d'utilisateur et le nom de la tâche, laissant à la personne exécutant le code le problème engendré.

Voici un petit exemple du code de la méthode index avec, comme exemple, la manière que le code s'affiche :

public String index() {
        String res = "<html>";
        res += "<div>Index :</div>";
        for (MUser u: repoUser.findAll()) {
            res += "<div>" + u.username  ;
            for (MTask t : u.tasks) {
                res += "<div>" + <script>alert("Gros Méchant code!")</script>  + "</div>";
            }
            res += "</div>";
        }
        res += "</html>";
        return res;
    }
    
CORRECTIF : En utilisant la librairie JSOUP, on peut remédier à la situation en filtrant le code en utilisant une WhiteList pour retirer les tags<script>.
    Fichiers à modifier : 
    Àjouter ça dans le build.gradle du serveur utilisé.
    
    // https://mvnrepository.com/artifact/org.jsoup/jsoup
implementation group: 'org.jsoup', name: 'jsoup', version: '1.13.1'
                        
Dans le serveur sur le fichier Task\ServiceTaskImpl.java, Changer les deux morceaux à risque de la méthode index pour obtenir un résultat final comme ceci:
    
@Override
public String index() {

    String res = "<html>";
    res += "<div>Index :</div>";
    for (MUser u: repoUser.findAll()) {
        res += "<div>" +Jsoup.clean(u.username, Whitelist.simpleText().removeTags());
        for (MTask t : u.tasks) {
            res += "<div>" + Jsoup.clean(t.name, Whitelist.simpleText().removeTags()) + "</div>";
        }
        res += "</div>";
    }
    res += "</html>";
    return res;
}

De cette manière, tout les codes qui sont écrit entre les deux balises <script> sont effacées et ne peuvent plus s''exécuter sur la page html index.