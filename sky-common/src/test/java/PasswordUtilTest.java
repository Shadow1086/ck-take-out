import com.ck.it.utils.PasswordUtil;
import org.junit.jupiter.api.Test;

/**
 * Package: PACKAGE_NAME
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/2 00:23
 */

public class PasswordUtilTest {
	@Test
	public void test01() {
		System.out.println(PasswordUtil.encode("123456"));
	}
}
