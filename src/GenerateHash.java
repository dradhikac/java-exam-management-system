import util.PasswordUtil;

public class GenerateHash {
    public static void main(String[] args) {
        System.out.println(PasswordUtil.hashPassword("admin123"));
        System.out.println(PasswordUtil.hashPassword("hod123"));
        System.out.println(PasswordUtil.hashPassword("prof123"));
        System.out.println(PasswordUtil.hashPassword("exam123"));
    }
}
