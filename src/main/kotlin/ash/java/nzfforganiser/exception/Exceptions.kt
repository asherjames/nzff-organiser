package ash.java.nzfforganiser.exception

class ScraperException : Exception {
  constructor() : super()
  constructor(message: String) : super(message)
  constructor(cause: Throwable) : super(cause)
  constructor(message: String, cause: Throwable) : super(message, cause)
}

class TooManySchedulesException : Exception {
  constructor() : super()
  constructor(message: String) : super(message)
  constructor(cause: Throwable) : super(cause)
  constructor(message: String, cause: Throwable) : super(message, cause)
}

class NoAcceptableScheduleFoundException : Exception {
  constructor() : super()
  constructor(message: String) : super(message)
  constructor(cause: Throwable) : super(cause)
  constructor(message: String, cause: Throwable) : super(message, cause)
}
