package ash.java.nzfforganiser.schedule

import ash.java.nzfforganiser.exception.NoAcceptableScheduleFoundException
import ash.java.nzfforganiser.model.Cinema
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.ScheduleFilter
import ash.java.nzfforganiser.model.ScheduleRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NzffSchedulerTest
{
  private val scheduler = NzffSchedulerImpl()

  private val mondayMorning = LocalDateTime.parse("2018-06-18T09:00:00")
  private val mondayAfternoon = LocalDateTime.parse("2018-06-18T14:00:00")
  private val mondayEvening = LocalDateTime.parse("2018-06-18T19:00:00")

  private val tuesdayMorning = LocalDateTime.parse("2018-06-19T09:00:00")
  private val tuesdayAfternoon = LocalDateTime.parse("2018-06-19T14:00:00")
  private val tuesdayEvening = LocalDateTime.parse("2018-06-19T19:00:00")

  private val wednesdayMorning = LocalDateTime.parse("2018-06-20T09:00:00")
  private val wednesdayAfternoon = LocalDateTime.parse("2018-06-20T14:00:00")
  private val wednesdayEvening = LocalDateTime.parse("2018-06-20T19:00:00")

  private val thursdayMorning = LocalDateTime.parse("2018-06-21T09:00:00")
  private val thursdayAfternoon = LocalDateTime.parse("2018-06-21T14:00:00")
  private val thursdayEvening = LocalDateTime.parse("2018-06-21T19:00:00")

  private val fridayMorning = LocalDateTime.parse("2018-06-22T09:00:00")
  private val fridayAfternoon = LocalDateTime.parse("2018-06-22T14:00:00")
  private val fridayEvening = LocalDateTime.parse("2018-06-22T19:00:00")

  @Test
  fun `excluded days are rejected`()
  {
    val schedule = mutableListOf(
        createMovie("A", mondayEvening, mondayEvening.plusHours(2)),
        createMovie("B", fridayEvening, fridayEvening.plusHours(2)),
        createMovie("C", fridayMorning, fridayMorning.plusHours(2))
    )

    val filters = listOf(ScheduleFilter(DayOfWeek.MONDAY, excluded = true)).toExcludedDays()

    val evaluations = schedule.map { m -> scheduler.isOnValidDay(m, filters) }

    assertThat(evaluations).containsExactly(false, true, true)
  }

  @Test
  fun `acceptable days are not rejected`()
  {
    val schedule = mutableListOf(
        createMovie("A", mondayEvening, mondayEvening.plusHours(2)),
        createMovie("B", fridayEvening, fridayEvening.plusHours(2)),
        createMovie("C", fridayMorning, fridayMorning.plusHours(2))
    )

    val filters = listOf(ScheduleFilter(DayOfWeek.TUESDAY, excluded = true)).toExcludedDays()

    assertThat(schedule).allSatisfy { m -> !scheduler.isOnValidDay(m, filters) }
  }

  @Test
  fun `unacceptable session times are rejected`()
  {
    val schedule = mutableListOf(
        createMovie("A", mondayEvening, mondayEvening.plusHours(2)),
        createMovie("B", fridayEvening, fridayEvening.plusHours(2)),
        createMovie("C", fridayMorning, fridayMorning.plusHours(2))
    )

    val filters = listOf(ScheduleFilter(
        DayOfWeek.FRIDAY,
        from = LocalTime.parse("09:30:00"),
        to = LocalTime.parse("17:30:00")
    )).toExcludedPeriods()

    assertThat(schedule).allSatisfy { m -> !scheduler.isInValidPeriod(m, filters) }
  }

  @Test
  fun `partially acceptable session times are still rejected`()
  {
    val schedule = mutableListOf(
        createMovie("A", wednesdayMorning, wednesdayMorning.plusHours(2)),
        createMovie("B", wednesdayAfternoon, wednesdayAfternoon.plusHours(2)),
        createMovie("C", wednesdayEvening, wednesdayEvening.plusHours(2))
    )

    val filters = listOf(ScheduleFilter(
        DayOfWeek.WEDNESDAY,
        from = LocalTime.parse("09:00:00"),
        to = LocalTime.parse("20:59:00")
    )).toExcludedPeriods()

    assertThat(schedule).allSatisfy { m -> !scheduler.isInValidPeriod(m, filters) }
  }

  @Test
  fun `restrictions on irrelevant days are ignored`()
  {
    val schedule = mutableListOf(
        createMovie("A", mondayEvening, mondayEvening.plusHours(2)),
        createMovie("B", fridayEvening, fridayEvening.plusHours(2)),
        createMovie("C", fridayMorning, fridayMorning.plusHours(2))
    )

    val filters = listOf(ScheduleFilter(
        DayOfWeek.THURSDAY,
        from = LocalTime.parse("09:30:00"),
        to = LocalTime.parse("17:30:00")
    )).toExcludedPeriods()

    assertThat(schedule).allSatisfy { m -> !scheduler.isInValidPeriod(m, filters) }
  }

  @Test
  fun `restrictions that match sessions exactly are accepted`()
  {
    val schedule = mutableListOf(
        createMovie("A", mondayEvening, mondayEvening.plusHours(2)),
        createMovie("B", tuesdayEvening, tuesdayEvening.plusHours(2)),
        createMovie("C", fridayMorning, fridayMorning.plusHours(2)),
        createMovie("D", fridayAfternoon, fridayAfternoon.plusHours(2)),
        createMovie("E", fridayEvening, fridayEvening.plusHours(2))
    )

    val filters = listOf(
        ScheduleFilter(
            DayOfWeek.TUESDAY,
            from = LocalTime.parse("18:30:00"),
            to = LocalTime.parse("21:30:00")
        ),
        ScheduleFilter(
            DayOfWeek.THURSDAY,
            from = LocalTime.parse("09:30:00"),
            to = LocalTime.parse("17:30:00")
        ),
        ScheduleFilter(
            DayOfWeek.FRIDAY,
            from = LocalTime.parse("09:00:00"),
            to = LocalTime.parse("21:00:00")
        )).toExcludedPeriods()

    assertThat(schedule).allSatisfy { m -> !scheduler.isInValidPeriod(m, filters) }
  }

  @Test
  fun `sessions that do not clash are accepted`()
  {
    val schedule = mutableListOf(
        createMovie("A", mondayEvening, mondayEvening.plusHours(2)),
        createMovie("B", tuesdayEvening, tuesdayEvening.plusHours(2)),
        createMovie("C", fridayMorning, fridayMorning.plusHours(2)),
        createMovie("D", fridayAfternoon, fridayAfternoon.plusHours(2)),
        createMovie("E", fridayEvening, fridayEvening.plusHours(2))
    )

    assertThat(scheduler.hasClashingSessions(schedule, 0)).isFalse()
  }

  @Test
  fun `sessions that clash are rejected`()
  {
    val schedule = mutableListOf(
        createMovie("A", mondayEvening, mondayEvening.plusHours(2)),
        createMovie("B", tuesdayEvening, tuesdayEvening.plusHours(2)),
        createMovie("C", fridayMorning, fridayMorning.plusHours(2)),
        createMovie("D", fridayEvening.minusHours(1), fridayEvening.plusHours(1)),
        createMovie("E", fridayEvening, fridayEvening.plusHours(2))
    )

    assertThat(scheduler.hasClashingSessions(schedule, 0)).isTrue()
  }

  @Test
  fun `schedules with excluded days throw correct exception`()
  {
    val movies = listOf(
        listOf(
            createMovie("A", mondayEvening, mondayEvening.plusHours(2)),
            createMovie("A", tuesdayMorning, tuesdayMorning.plusHours(2))
        ),
        listOf(
            createMovie("B", fridayEvening, fridayEvening.plusHours(2))
        ),
        listOf(
            createMovie("C", fridayEvening, fridayEvening.plusHours(2)),
            createMovie("C", fridayMorning, fridayMorning.plusHours(2))
        )
    )

    val filters = listOf(
        ScheduleFilter(DayOfWeek.MONDAY, excluded = true),
        ScheduleFilter(DayOfWeek.TUESDAY, excluded = true),
        ScheduleFilter(DayOfWeek.FRIDAY, excluded = true)
    )

    assertThrows<NoAcceptableScheduleFoundException> { scheduler.findSchedule(movies,
        ScheduleRequest(scheduleFilters = filters)) }
  }

  @Test
  fun `schedules with no excluded days are accepted`()
  {
    val movies = listOf(
        listOf(
            createMovie("A1", mondayEvening, mondayEvening.plusHours(2)),
            createMovie("A2", tuesdayMorning, tuesdayMorning.plusHours(2))
        ),
        listOf(
            createMovie("B", fridayEvening, fridayEvening.plusHours(2))
        ),
        listOf(
            createMovie("C1", fridayAfternoon, fridayAfternoon.plusHours(2)),
            createMovie("C2", fridayMorning, fridayMorning.plusHours(2))
        )
    )

    val filters = listOf(
        ScheduleFilter(DayOfWeek.MONDAY, excluded = true)
    )

    assertThat(scheduler.findSchedule(movies, ScheduleRequest(scheduleFilters = filters))
        .scheduleSuggestion
        .map { m -> m.title })
        .containsExactly("A2", "B", "C1")
  }

  @Test
  fun `schedules with excluded periods throw correct exception`()
  {
    val movies = listOf(
        listOf(
            createMovie("A1", mondayEvening, mondayEvening.plusHours(2)),
            createMovie("A2", mondayAfternoon, mondayAfternoon.plusHours(2))
        ),
        listOf(
            createMovie("B1", tuesdayEvening, tuesdayEvening.plusHours(2)),
            createMovie("B2", tuesdayMorning, tuesdayMorning.plusHours(2))
        ),
        listOf(createMovie("C", fridayMorning, fridayMorning.plusHours(2))),
        listOf(createMovie("D", fridayAfternoon, fridayAfternoon.plusHours(2))),
        listOf(createMovie("E", fridayEvening, fridayEvening.plusHours(2)))
    )

    val filters = listOf(
        ScheduleFilter(
            DayOfWeek.MONDAY,
            from = LocalTime.parse("14:30:00"),
            to = LocalTime.parse("15:30:00")
        ),
        ScheduleFilter(
            DayOfWeek.TUESDAY,
            from = LocalTime.parse("10:30:00"),
            to = LocalTime.parse("20:30:00")
        ),
        ScheduleFilter(
            DayOfWeek.THURSDAY,
            from = LocalTime.parse("09:30:00"),
            to = LocalTime.parse("17:30:00")
        ),
        ScheduleFilter(
            DayOfWeek.FRIDAY,
            from = LocalTime.parse("08:45:00"),
            to = LocalTime.parse("10:00:00")
        ))

    assertThrows<NoAcceptableScheduleFoundException> { scheduler.findSchedule(movies,
        ScheduleRequest(scheduleFilters = filters)) }
  }

  @Test
  fun `schedules with no excluded periods are accepted`()
  {
    val movies = listOf(
        listOf(
            createMovie("A1", mondayEvening, mondayEvening.plusHours(2)),
            createMovie("A2", mondayAfternoon, mondayAfternoon.plusHours(2))
        ),
        listOf(
            createMovie("B1", tuesdayEvening, tuesdayEvening.plusHours(2)),
            createMovie("B2", tuesdayMorning, tuesdayMorning.plusHours(2))
        ),
        listOf(
            createMovie("C1", thursdayMorning, thursdayMorning.plusHours(2)),
            createMovie("C2", thursdayAfternoon, thursdayAfternoon.plusHours(2)),
            createMovie("C3", thursdayEvening, thursdayEvening.plusHours(2))
        ),
        listOf(createMovie("D", fridayAfternoon, fridayAfternoon.plusHours(2))),
        listOf(createMovie("E", fridayEvening, fridayEvening.plusHours(2)))
    )

    val filters = listOf(
        ScheduleFilter(
            DayOfWeek.MONDAY,
            from = LocalTime.parse("14:00:00"),
            to = LocalTime.parse("16:30:00")
        ),
        ScheduleFilter(
            DayOfWeek.TUESDAY,
            from = LocalTime.parse("10:30:00"),
            to = LocalTime.parse("20:30:00")
        ),
        ScheduleFilter(
            DayOfWeek.THURSDAY,
            from = LocalTime.parse("19:00:00"),
            to = LocalTime.parse("21:00:00")
        ),
        ScheduleFilter(
            DayOfWeek.FRIDAY,
            from = LocalTime.parse("08:45:00"),
            to = LocalTime.parse("22:00:00")
        ))

    assertThat(scheduler.findSchedule(movies, ScheduleRequest(scheduleFilters = filters))
        .scheduleSuggestion
        .map { m -> m.title })
        .containsExactly("A2", "C3", "D", "E")
  }

  @Test
  fun `filters with default values should have no effect`()
  {
    val movies = listOf(
        listOf(
            createMovie("A1", mondayEvening, mondayEvening.plusHours(2)),
            createMovie("A2", mondayAfternoon, mondayAfternoon.plusHours(2))
        ),
        listOf(
            createMovie("B1", tuesdayEvening, tuesdayEvening.plusHours(2)),
            createMovie("B2", tuesdayMorning, tuesdayMorning.plusHours(2))
        ),
        listOf(createMovie("C", fridayMorning, fridayMorning.plusHours(2))),
        listOf(createMovie("D", fridayAfternoon, fridayAfternoon.plusHours(2))),
        listOf(createMovie("E", fridayEvening, fridayEvening.plusHours(2)))
    )

    val filters = listOf(
        ScheduleFilter(
            DayOfWeek.MONDAY,
            from = LocalTime.parse("00:00:00"),
            to = LocalTime.parse("23:59:59")
        ),
        ScheduleFilter(
            DayOfWeek.TUESDAY,
            from = LocalTime.parse("00:00:00"),
            to = LocalTime.parse("23:59:59")
        ),
        ScheduleFilter(
            DayOfWeek.WEDNESDAY,
            from = LocalTime.parse("00:00:00"),
            to = LocalTime.parse("23:59:59")
        ),
        ScheduleFilter(
            DayOfWeek.THURSDAY,
            from = LocalTime.parse("00:00:00"),
            to = LocalTime.parse("23:59:59")
        ),
        ScheduleFilter(
            DayOfWeek.FRIDAY,
            from = LocalTime.parse("00:00:00"),
            to = LocalTime.parse("23:59:59")
        ),
        ScheduleFilter(
            DayOfWeek.SATURDAY,
            from = LocalTime.parse("00:00:00"),
            to = LocalTime.parse("23:59:59")
        ),
        ScheduleFilter(
            DayOfWeek.SUNDAY,
            from = LocalTime.parse("00:00:00"),
            to = LocalTime.parse("23:59:59")
        )
    )

    assertThat(scheduler.findSchedule(movies, ScheduleRequest(scheduleFilters = filters))
        .scheduleSuggestion
        .map { m -> m.title })
        .containsExactly("A1", "B1", "C", "D", "E")
  }

  @Test
  fun `single movie is processed correctly`()
  {
    val movies = listOf(
        listOf(
            createMovie("A", mondayMorning, mondayMorning.plusHours(2))
        )
    )

    assertThat(scheduler.findSchedule(movies, ScheduleRequest()).scheduleSuggestion)
        .containsOnly(createMovie("A", mondayMorning, mondayMorning.plusHours(2)))
  }

  @Test
  fun `movies with exactly the same times are skipped`()
  {
    val movies = listOf(
        listOf(
            createMovie("A1", mondayEvening, mondayEvening.plusHours(2)),
            createMovie("A2", mondayAfternoon, mondayAfternoon.plusHours(2))
        ),
        listOf(createMovie("B", mondayEvening, mondayEvening.plusHours(2))),
        listOf(createMovie("C", fridayMorning, fridayMorning.plusHours(2)))
    )

    assertThat(scheduler.findSchedule(movies, ScheduleRequest())
        .scheduleSuggestion
        .map { m -> m.title })
        .containsExactly("A2", "B", "C")
  }

  @Test
  fun `unavailable movies are correctly identified`()
  {
    val movies = listOf(
        listOf(
            createMovie("A", mondayEvening, mondayEvening.plusHours(2)),
            createMovie("A", mondayAfternoon, mondayAfternoon.plusHours(2))
        ),
        listOf(createMovie("B", mondayEvening, mondayEvening.plusHours(2))),
        listOf(createMovie("C", fridayMorning, fridayMorning.plusHours(2))),
        listOf(
            createMovie("D", thursdayMorning, thursdayMorning.plusHours(2)),
            createMovie("D", thursdayAfternoon, thursdayAfternoon.plusHours(2)),
            createMovie("D", thursdayEvening, thursdayEvening.plusHours(2))
        )
    )

    val filters = listOf(ScheduleFilter(DayOfWeek.THURSDAY, excluded = true))

    val result = scheduler.findSchedule(movies, ScheduleRequest(scheduleFilters = filters))

    assertThat(result.scheduleSuggestion.map { m -> m.title })
        .containsExactly("A", "B", "C")

    assertThat(result.unavailableMovies)
        .containsExactly("D")
  }

  @Test
  fun `movies at excluded cinemas are filtered out`()
  {
    val movies = listOf(
        listOf(
            createMovieAtCinema("A1", mondayEvening, mondayEvening.plusHours(2), Cinema.HOLLYWOOD),
            createMovieAtCinema("A2", mondayAfternoon, mondayAfternoon.plusHours(2), Cinema.CIVIC)
        ),
        listOf(createMovieAtCinema("B", mondayEvening, mondayEvening.plusHours(2), Cinema.ACADEMY)),
        listOf(createMovieAtCinema("C", fridayMorning, fridayMorning.plusHours(2), Cinema.RIALTO)),
        listOf(
            createMovieAtCinema("D1", thursdayMorning, thursdayMorning.plusHours(2), Cinema.HOLLYWOOD),
            createMovieAtCinema("D2", thursdayAfternoon, thursdayAfternoon.plusHours(2), Cinema.RIALTO),
            createMovieAtCinema("D3", thursdayEvening, thursdayEvening.plusHours(2), Cinema.UNKNOWN)
        )
    )

    val excludedCinemas = listOf(Cinema.HOLLYWOOD, Cinema.RIALTO)

    assertThat(scheduler.findSchedule(movies, ScheduleRequest(excludedCinemas = excludedCinemas))
        .scheduleSuggestion
        .map { m -> m.title })
        .containsExactly("A2", "B", "D3")
  }

  @Test
  fun `movies closer than session gap are skipped`()
  {
    val schedule = mutableListOf(
        createMovie("A", mondayEvening, mondayEvening.plusHours(2)),
        createMovie("B", tuesdayEvening, tuesdayEvening.plusHours(2)),
        createMovie("C", fridayMorning, fridayMorning.plusHours(2)),
        createMovie("D", fridayMorning.plusMinutes(140), fridayMorning.plusMinutes(240)),
        createMovie("E", fridayEvening, fridayEvening.plusHours(2))
    )

    assertThat(scheduler.hasClashingSessions(schedule, 30)).isTrue()
  }

  @Test
  fun `schedules that violate session gap are skipped`()
  {
    val movies = listOf(
        listOf(
            createMovieAtCinema("A1", mondayEvening, mondayEvening.plusHours(2), Cinema.HOLLYWOOD),
            createMovieAtCinema("A2", mondayAfternoon, mondayAfternoon.plusHours(2), Cinema.CIVIC)
        ),
        listOf(createMovieAtCinema("B", mondayEvening.plusMinutes(130), mondayEvening.plusMinutes(230), Cinema.ACADEMY)),
        listOf(createMovieAtCinema("C", fridayAfternoon.plusMinutes(125), fridayAfternoon.plusMinutes(225), Cinema.RIALTO)),
        listOf(
            createMovieAtCinema("D1", mondayAfternoon.plusMinutes(14), mondayAfternoon.plusMinutes(114), Cinema.HOLLYWOOD),
            createMovieAtCinema("D2", fridayAfternoon, fridayAfternoon.plusHours(2), Cinema.RIALTO),
            createMovieAtCinema("D3", thursdayEvening, thursdayEvening.plusHours(2), Cinema.UNKNOWN)
        )
    )

    assertThat(scheduler.findSchedule(movies, ScheduleRequest(sessionGap = 15))
        .scheduleSuggestion
        .map { m -> m.title })
        .containsExactly("A2", "B", "C", "D3")
  }

  private fun createMovie(title: String, startTime: LocalDateTime, endTime: LocalDateTime): Movie
  {
    return Movie(title = title,
        websiteUrl = "",
        thumbnailUrl = "",
        startTime = startTime,
        endTime = endTime)
  }

  private fun createMovieAtCinema(title: String, startTime: LocalDateTime, endTime: LocalDateTime, cinema: Cinema): Movie
  {
    return Movie(title = title,
        websiteUrl = "",
        thumbnailUrl = "",
        startTime = startTime,
        endTime = endTime,
        cinema = cinema)
  }

  private fun List<ScheduleFilter>.toExcludedDays(): List<DayOfWeek>
  {
    return this
        .filter { f -> f.excluded }
        .map { f -> f.day }
  }

  private fun List<ScheduleFilter>.toExcludedPeriods(): Map<DayOfWeek, Pair<LocalTime, LocalTime>>
  {
    return this.map { it.day to Pair(it.from, it.to) }
        .toMap()
  }
}