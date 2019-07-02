package ash.java.nzfforganiser

import ash.java.nzfforganiser.exception.NoAcceptableScheduleFoundException
import ash.java.nzfforganiser.exception.ScraperException
import ash.java.nzfforganiser.exception.TooManySchedulesException
import ash.java.nzfforganiser.model.NzffResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionAdvisor : ResponseEntityExceptionHandler() {
  @ExceptionHandler(ScraperException::class)
  fun handleScraperException(e: ScraperException): ResponseEntity<NzffResponse> {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(NzffResponse(
            message = e.message ?: ""
        ))
  }

  @ExceptionHandler(TooManySchedulesException::class)
  fun handleTooManyResultsException(e: TooManySchedulesException): ResponseEntity<NzffResponse> {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(NzffResponse(
            message = "There are too many scheduling options, please select less films"
        ))
  }

  @ExceptionHandler(NoAcceptableScheduleFoundException::class)
  fun handleNoAcceptableScheduleFound(e: NoAcceptableScheduleFoundException): ResponseEntity<NzffResponse> {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(NzffResponse(
            message = "Could not find a suitable session schedule"
        ))
  }

  @ExceptionHandler(Exception::class)
  fun handleAll(e: Exception): ResponseEntity<NzffResponse> {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(NzffResponse(
            message = "Internal error has occured, check logs!"
        ))
  }
}