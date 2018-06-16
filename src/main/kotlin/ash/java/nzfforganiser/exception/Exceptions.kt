package ash.java.nzfforganiser.exception

class ScraperException : Exception
{
  constructor(message: String) : super(message)
  constructor(cause: Throwable) : super(cause)
  constructor(message: String, cause: Throwable) : super(message, cause)
}

class TooManySchedulesException : Exception
{
  constructor(message: String) : super(message)
  constructor(cause: Throwable) : super(cause)
  constructor(message: String, cause: Throwable) : super(message, cause)
}