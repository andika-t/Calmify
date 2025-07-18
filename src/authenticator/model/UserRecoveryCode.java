package authenticator.model;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserRecoveryCode {
    
    public List<String> generateRecoveryCodes(int count, int length) {
        final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();

        return IntStream.range(0, count)
                .mapToObj(i -> random.ints(length, 0, CHARS.length())
                        .mapToObj(CHARS::charAt)
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString())
                .collect(Collectors.toList());
    }

}
