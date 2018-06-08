package ash.java.nzfforganiser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class NzffOrganiserApplication

fun main(args: Array<String>)
{
  runApplication<NzffOrganiserApplication>(*args)
}
