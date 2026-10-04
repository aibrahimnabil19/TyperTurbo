import java.util.Map;

public final class I18n {
    public static final String ENGLISH = "English";
    public static final String FRENCH = "French";
    private static volatile String language = ENGLISH;

    private static final Map<String, String> FRENCH_TEXT = Map.ofEntries(
            Map.entry("Start", "Démarrer"),
            Map.entry("HOMEPAGE", "ACCUEIL"),
            Map.entry("Français", "Français"),
            Map.entry("Enter your username:", "Saisissez votre nom d’utilisateur :"),
            Map.entry("Choose your user:", "Choisissez votre utilisateur :"),
            Map.entry("User Selection", "Sélection de l’utilisateur"),
            Map.entry("Username cannot be empty.", "Le nom d’utilisateur ne peut pas être vide."),
            Map.entry("Username already exists! Please choose another.", "Ce nom d’utilisateur existe déjà ! Choisissez-en un autre."),
            Map.entry("Could not validate username: ", "Impossible de vérifier le nom d’utilisateur : "),
            Map.entry("+ Add new user…", "+ Ajouter un utilisateur…"),
            Map.entry("Settings", "Paramètres"),
            Map.entry("Back", "Retour"),
            Map.entry("Resume", "Reprendre"),
            Map.entry("Quit", "Quitter"),
            Map.entry("Homepage", "Accueil"),
            Map.entry("Exit Game", "Quitter le jeu"),
            Map.entry("What would you like to do?", "Que souhaitez-vous faire ?"),
            Map.entry("Quit Game", "Quitter le jeu"),
            Map.entry("Time:", "Temps :"),
            Map.entry("SCORE:", "SCORE :"),
            Map.entry("Score:", "Score :"),
            Map.entry("Time's up!", "Temps écoulé !"),
            Map.entry("High Score:", "Meilleur score :"),
            Map.entry("Error retrieving score:", "Erreur lors de la récupération du score :"),
            Map.entry("Database Error", "Erreur de base de données"),
            Map.entry("Level Up", "Niveau supérieur"),
            Map.entry("Endless", "Sans fin"),
            Map.entry("Clock", "Contre-la-montre"),
            Map.entry("Vs Clock", "Contre-la-montre"),
            Map.entry("Challenge", "Défi"),
            Map.entry("GAMEMODE", "MODE DE JEU"),
            Map.entry("Select your difficulty:", "Choisissez la difficulté :"),
            Map.entry("Easy", "Facile"),
            Map.entry("Intermediate", "Intermédiaire"),
            Map.entry("Hard", "Difficile"),
            Map.entry("Pro", "Expert"),
            Map.entry("Level", "Niveau"),
            Map.entry("Reached!", " atteint !"),
            Map.entry("Game Over!", "Fin de partie !"),
            Map.entry("Your Score:", "Votre score :"),
            Map.entry("Success", "Réussi"),
            Map.entry("Failed", "Échec"),
            Map.entry("Correct!", "Correct !"),
            Map.entry("Incorrect!", "Incorrect !"),
            Map.entry("Try Again.", "Réessayez."),
            Map.entry("Correct", "Correct"),
            Map.entry("Wrong!", "Faux !"),
            Map.entry("Wrong", "Faux"),
            Map.entry("Right!", "Bonne réponse !"),
            Map.entry("Nice!", "Bravo !"),
            Map.entry("Nope", "Non"),
            Map.entry("Good!", "Bien joué !"),
            Map.entry("Think Emoji.", "Pensez aux émojis."),
            Map.entry("Avoid the letter: ", "Évitez la lettre : "),
            Map.entry("You used the forbidden letter!", "Vous avez utilisé la lettre interdite !"),
            Map.entry("Well done!", "Bravo !"),
            Map.entry("Your input: ", "Votre réponse : "),
            Map.entry("Typing Challenge Mode", "Mode défi de frappe"),
            Map.entry("Challenge Modes", "Modes de défi"),
            Map.entry("Start Challenge", "Commencer le défi"),
            Map.entry("Cancel", "Annuler"),
            Map.entry("Paused", "En pause"),
            Map.entry("Back to Challenges", "Retour aux défis"),
            Map.entry("Switch language", "Changer de langue"),
            Map.entry("Play: ", "Jouer : "),
            Map.entry("Profile", "Profil"),
            Map.entry("Typing Statistics", "Statistiques de frappe"),
            Map.entry("Game Modes", "Modes de jeu"),
            Map.entry("Themes", "Thèmes"),
            Map.entry("Sound & Effects", "Son et effets"),
            Map.entry("Credits", "Crédits"),
            Map.entry("Help & Support", "Aide et assistance"),
            Map.entry("Username:", "Nom d’utilisateur :"),
            Map.entry("Rename My Account", "Renommer mon compte"),
            Map.entry("Delete My Account", "Supprimer mon compte"),
            Map.entry("Enter your new username:", "Saisissez votre nouveau nom d’utilisateur :"),
            Map.entry("Username changed!", "Nom d’utilisateur modifié !"),
            Map.entry("Failed to rename (maybe already in use).", "Impossible de renommer le compte (nom peut-être déjà utilisé)."),
            Map.entry("Are you sure you want to delete your account?\nThis action cannot be undone.", "Voulez-vous vraiment supprimer votre compte ?\nCette action est irréversible."),
            Map.entry("Confirm Deletion", "Confirmer la suppression"),
            Map.entry("Account deleted.", "Compte supprimé."),
            Map.entry("Date", "Date"),
            Map.entry("WPM", "Mots/min"),
            Map.entry("Accuracy", "Précision"),
            Map.entry("Errors", "Erreurs"),
            Map.entry("Words", "Mots"),
            Map.entry("Averages over ", "Moyennes sur "),
            Map.entry(" sessions:", " sessions :"),
            Map.entry("No typing sessions recorded yet.", "Aucune session de frappe enregistrée."),
            Map.entry("Choose theme:", "Choisissez un thème :"),
            Map.entry("Background Music", "Musique de fond"),
            Map.entry("Effects Sounds", "Effets sonores"),
            Map.entry("Coming soon...", "Bientôt disponible..."),
            Map.entry("Send Feedback", "Envoyer un commentaire"),
            Map.entry("Describe your issue or suggestion", "Décrivez votre problème ou votre suggestion"),
            Map.entry("Please enter a message.", "Veuillez saisir un message."),
            Map.entry("No Content", "Aucun contenu"),
            Map.entry("Thank you! Your feedback was sent.", "Merci ! Votre commentaire a été envoyé."),
            Map.entry("Sent", "Envoyé"),
            Map.entry("Failed to send feedback:\n", "Échec de l’envoi du commentaire :\n"),
            Map.entry("Mail Error", "Erreur d’envoi"),
            Map.entry("Report a Problem/Ask a qstn", "Signaler un problème / poser une question"),
            Map.entry("Could not launch email client.\nPlease send to contact@aishatek.com.", "Impossible d’ouvrir le client de messagerie.\nÉcrivez à contact@aishatek.com."),
            Map.entry("Error", "Erreur"),
            Map.entry("Version 1.0", "Version 1.0"),
            Map.entry("copyright @Aishatek 2025", "Copyright © Aishatek 2025"),
            Map.entry("Language", "Langue"),
            Map.entry("French", "Français"),
            Map.entry("English", "Anglais"),
            Map.entry("Clock Instructions", "Instructions du contre-la-montre"),
            Map.entry("Level Up Instructions", "Instructions du mode Niveau supérieur"),
            Map.entry("Endless Instructions", "Instructions du mode Sans fin"),
            Map.entry("Challenge Instructions", "Instructions du mode Défi"),
            Map.entry("Time: %.1fs", "Temps : %.1fs"),
            Map.entry("Success in ", "Réussi en "),
            Map.entry("High Score: 0", "Meilleur score : 0")
            ,Map.entry("Instructions", "Instructions")
            ,Map.entry("Choose language:", "Choisissez la langue :")
            ,Map.entry("Play", "Jouer")
            ,Map.entry("Mode", "Mode")
            ,Map.entry("Pause", "Pause")
            ,Map.entry("Type: ", "Saisie : ")
            ,Map.entry("Success!", "Réussi !")
            ,Map.entry("Failed!", "Échec !")
            ,Map.entry("No such user—nothing to do", "Utilisateur introuvable.")
            ,Map.entry("Speed Demon", "Vitesse éclair")
            ,Map.entry("Typo Trouble", "Chasse aux fautes")
            ,Map.entry("Word Bomb", "Bombe de mots")
            ,Map.entry("Disappearing Text", "Texte éphémère")
            ,Map.entry("Scramble Sprint", "Mots mélangés")
            ,Map.entry("Ghost Keys", "Touches fantômes")
            ,Map.entry("Falling Words", "Mots qui tombent")
            ,Map.entry("Reversal", "À l’envers")
            ,Map.entry("One-Hand Challenge", "Défi à une main")
            ,Map.entry("Blind Type", "Frappe à l’aveugle")
            ,Map.entry("AutoCorrect Chaos", "Correcteur fou")
            ,Map.entry("Typing Trivia", "Quiz de frappe")
            ,Map.entry("Morse Mayhem", "Morse express")
            ,Map.entry("Emoji Words", "Mots en émojis")
            ,Map.entry("Forbidden Letter", "Lettre interdite")
            ,Map.entry("Beat Autocorrect!", "Battez le correcteur !")
            ,Map.entry("Close, but no.", "Presque, mais non.")
            ,Map.entry("High Score: ", "Meilleur score : ")
            ,Map.entry("WPM: %.1f", "Mots/min : %.1f")
            ,Map.entry("Accuracy: %.1f%%", "Précision : %.1f%%")
            ,Map.entry("Errors: %.1f", "Erreurs : %.1f")
            ,Map.entry("Words: %.1f", "Mots : %.1f")
            ,Map.entry("Could not load typing stats:\n", "Impossible de charger les statistiques :\n")
            ,Map.entry("Select language:", "Choisissez la langue :")
            ,Map.entry("New user", "Nouvel utilisateur")
            ,Map.entry(" in ", " en ")
            ,Map.entry("Time: 0s", "Temps : 0 s")
    );

    private I18n() {}

    public static void setLanguage(String newLanguage) {
        language = FRENCH.equalsIgnoreCase(newLanguage) || "Français".equalsIgnoreCase(newLanguage)
                ? FRENCH : ENGLISH;
        PreferencesManager.saveLanguageChoice(language);
    }

    public static String getLanguage() {
        return language;
    }

    public static boolean isFrench() {
        return FRENCH.equals(language);
    }

    public static String t(String englishText) {
        if (!isFrench() || englishText == null) return englishText;
        String translated = FRENCH_TEXT.get(englishText);
        if (translated != null) return translated;
        Map.Entry<String, String> bestPrefix = null;
        for (Map.Entry<String, String> entry : FRENCH_TEXT.entrySet()) {
            if (englishText.startsWith(entry.getKey())
                    && (bestPrefix == null || entry.getKey().length() > bestPrefix.getKey().length())) {
                bestPrefix = entry;
            }
        }
        return bestPrefix == null ? englishText
                : bestPrefix.getValue() + englishText.substring(bestPrefix.getKey().length());
    }

    public static String toEnglishDifficulty(String displayedDifficulty) {
        if (displayedDifficulty == null || !isFrench()) return displayedDifficulty;
        return switch (displayedDifficulty) {
            case "Facile" -> "Easy";
            case "Intermédiaire" -> "Intermediate";
            case "Difficile" -> "Hard";
            case "Expert" -> "Pro";
            default -> displayedDifficulty;
        };
    }
}
