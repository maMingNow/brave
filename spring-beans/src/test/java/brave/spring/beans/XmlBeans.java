package brave.spring.beans;

import java.nio.charset.Charset;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

//可以组装成spring的配置信息
class XmlBeans extends AbstractXmlApplicationContext {
  static final Charset UTF_8 = Charset.forName("UTF-8");

  final ByteArrayResource resource;

  XmlBeans(String... beans) {
    StringBuilder joined = new StringBuilder();
    for (String bean : beans) {
      joined.append(bean).append('\n');
    }
    this.resource = new ByteArrayResource(beans(joined.toString()).getBytes(UTF_8));
  }

  @Override protected Resource[] getConfigResources() {
    return new Resource[] {resource};
  }

  static String beans(String bean) {
    return "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n"
        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
        + "    xmlns:util=\"http://www.springframework.org/schema/util\"\n"
        + "    xsi:schemaLocation=\"\n"
        + "        http://www.springframework.org/schema/beans\n"
        + "        http://www.springframework.org/schema/beans/spring-beans-3.2.xsd\n"
        + "        http://www.springframework.org/schema/util\n"
        + "        http://www.springframework.org/schema/util/spring-util-3.2.xsd\">\n"
        + bean
        + "</beans>";
  }
}
