import auth.AuthService;
import cli.MenuRenderer;
import model.User;
public class Main {
    public static void main(String[] args) {
        
        AuthService auth = new AuthService();
        MenuRenderer menu = new MenuRenderer();

        User user = null;

        while (user == null) {
            user = auth.login();
        }

        menu.showMenu(user);

        System.out.println("Goodbye.");
    }
}
