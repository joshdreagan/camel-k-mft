// camel-k: language=java

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class SftpGateway extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("sftp:{{sftp.host}}:{{sftp.port:22}}/{{sftp.path:./}}?username={{sftp.username}}&password=RAW({{sftp.password}})&delete=true")
      .log(LoggingLevel.INFO, "Transferring file: [${headers.CamelFileName}]...")
      .setHeader("CamelAwsS3Key").simple("${headers.CamelFileLastModified}-${headers.CamelFileNameOnly}")
      .to("aws2-s3:{{aws-s3.bucketName}}")
      .to("aws2-s3:{{aws-s3.bucketName}}?operation=createDownloadLink")
      .log(LoggingLevel.INFO, "Sending download link: [${body}]")
      .to("kafka:{{kafka.topic}}")
    ;
  }
}
