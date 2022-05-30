package solar;

import lombok.Data;

/**
 * @author Sxuet
 * @since 2022.05.30 23:31
 */
@Data
public class AutoLoginDTO {
  private String secretKey;
  private String sign;
  private String osVersion;
  private String appVersion;

  /** 默认登录C0006193S001-施雪婷的账号 */
  AutoLoginDTO() {
    appVersion = "1.0.27";
    osVersion = "10";
    secretKey =
        "v4.0|iFHm+vqja1+u3jw9lTZCbaWAsR26+HWwRrWU92csbrKI4uem0IvO6LI/YZWw2fJkuvGcECpqatITWAsoSxJlyM/ROHv0nrPLy26nGl1+H2nhQZYCdmZc5sy99GQzcTvmLEcAAX2yAh8Rp2VwfnH9+B9qmFqbUcV6KMQIGYz9NNNxgFRywwwRj3YysAB3GD5Wo+XcMESPxnLIK9u9XNoQRvtqyBekkWa6Ed6XIXg8FDjUiPoXtPVuI70dbzjJpq1QF4VlaS7I/B/1Z+GTxmYAtgnt0LDlm9+ZE7QObURqQ1PFGlP+r5hLW33Fl48MaDpxBR1huVtYgQ6mQg15OwNH1A6gsivODViHZ80tj+Vujq2ZcqOOR241Ky5yFaw+NArNt0StuTw4aIRQ0NnkLU3XnQwmFVUlle8TdTbNf3dmP9eZLDAPGEHDQTnKCQwr3mjV7B+SkxFx7PYEzUphBtg+ylWvGMMuLhfq3mZWCDwSJ3q0TzPQ2XgDBh3S2Mbis6bUBWGVgvi0CBsnuqA+l78KteiCaluMCXbfzBEoap2/Kp7fOxjJOnDvinlM+O9BbQjUwcx+Mw6aO6qpWjexYwYghY6j/E+aZ8DRSHdHdOKY/FQCpnmwA6zsW3JTYkbRTducd96tML3g3KlFZFVXvxzbyACSbBEovCS0QLVS/EG5ogE=";
    sign = "1eb29fc90e6551ab20de51f5056af71c";
  }
}
