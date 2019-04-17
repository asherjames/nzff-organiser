package ash.java.nzfforganiser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class NzffOrgApplication

fun main(args: Array<String>) {
  runApplication<NzffOrgApplication>(*args)
}
