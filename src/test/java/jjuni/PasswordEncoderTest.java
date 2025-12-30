package jjuni;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PasswordEncoderTest {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void testPasswordEncodingAndMatching() {
        // given
        String rawPassword = "1234";

        // when
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // then
        System.out.println("Encoded password: " + encodedPassword);
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();

        // 다른 비밀번호 비교
        assertThat(passwordEncoder.matches("wrong-password", encodedPassword)).isFalse();
    }

    @Test
    void testDifferentEncodingEachTime() {
        String password = "Aiuser!";

        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);

        for (int i = 1; i < 21; i++) {
            String hash3 = passwordEncoder.encode(password + i);
            System.out.println(hash3);
        }

//        System.out.println("" + hash1);

        // 해시 값은 다르지만
        assertThat(hash1).isNotEqualTo(hash2);

        // 둘 다 원래 비밀번호와 매칭됨
        assertThat(passwordEncoder.matches(password, hash1)).isTrue();
        assertThat(passwordEncoder.matches(password, hash2)).isTrue();
    }

}
