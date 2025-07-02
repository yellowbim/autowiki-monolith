package clabi.poc;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloServiceTest {

    @Test
    void testGreet() {
        HelloService service = new HelloService();
        String result = service.greet();
        assertThat(result).isEqualTo(("Hello!"));
    }
}
