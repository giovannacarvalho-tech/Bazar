
package sistema_bazar;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        System.out.println("senha teste123456:" + encoder.encode("123456"));
        System.out.println("Senha teste4567: " + encoder.encode("4567"));
        System.out.println("Senha testerecife09: " + encoder.encode("recife09"));
        System.out.println("Senha testeflores50: " + encoder.encode("flores50"));
        System.out.println("Senha testeamanha80: " + encoder.encode("amanha80"));

    }
}
