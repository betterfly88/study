package tdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.jodatime.api.Assertions.assertThat;
import static org.joda.time.DateTimeZone.UTC;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by betterFLY on 2018. 4. 14.
 * Github : http://github.com/betterfly88
 */

public class jodatime {


    /*
        java.util.Date가 불안정한 이유

        1. 불변 객체가 아니다( not immutable)
        불행히도 Java의 기본 날짜, 시간 클래스는 불변 객체가 아니다. 앞의 코드에서 Calendar 클래스에 set 메서드를 호출해서 날짜를 지정하고, 다시 같은 객체에 set(int,int) 메서드를 호출해서 수행한 날짜 연산 결과는 같은 인스턴스에 저장되었다. Date 클래스에도 값을 바꿀 수 있는 set 메서드가 존재한다. 이 때문에 Calendar 객체나 Date 객체가 여러 객체에서 공유되면 한 곳에서 바꾼 값이 다른 곳에 영향을 미치는 부작용이 생길 수 있다. 『Effective Java 2nd Edition』(2008)의 저자 Joshua Bloch도 Date 클래스는 불변 객체여야 했다고 지적했다.[18]
        이를 안전하게 구현하려면 이들 객체를 복사해서 반환하는 기법을 권장한다. <그림 1>에서 보이는 코드의 startTime 필드는 내부의 Date 객체를 외부에서 조작할 수 있기 때문에 악의적인 클라이언트 코드에 의해서 착취당할 수 있다. endTime 필드처럼 방어복사 기법을 써서 새로운 객체를 생성해서 반환하는 구현이 바람직하다.[19]

        2.int 상수 필드의 남용
        calendar.add(Calendar.SECOND, 2);
        첫 번째 파라미터에 Calendar.JUNE과 같이, 전혀 엉뚱한 상수가 들어가도 이를 컴파일 시점에서 확인할 방법이 없다. 이 뿐만 아니라 Calendar 클래스에는 많은 int 상수가 쓰였는데, 이어서 설명할 월, 요일 지정 등에서도 많은 혼란을 유발한다.

        3. 헷갈리는 월 지정
        calendar.set(1582, Calendar.OCTOBER , 4);
        그런데 월에 해당하는 Calendar.OCTOBER 값은 실제로는 '9'이다. JDK 1.0에서 Date 클래스는 1월을 0으로 표현했고, JDK 1.1부터 포함된 Calendar 클래스도 이러한 관례를 답습했다. 그래서 1582년 10월 4일을 표현하는 코드를 다음과 같이 쓰는 실수를 많은 개발자들이 반복하고 있다.

        4. 일관성 없는 요일 상수
        Calendar.get(Calendar.DAY_OF_WEEK) 함수에서 반환한 요일은 int 값으로, 일요일이 1로 표현된다. 따라서 수요일은 4이고, 보통 Calendar.WEDNESDAY 상수와 비교해서 확인한다. 그런데 calendar.getTime() 메서드로 Date 객체를 얻어와서 Date.getDay() 메서드로 요일을 구하면 일요일은 0, 수요일은 3이 된다. 두 개의 클래스 사이에 요일 지정값에 일관성이 없는 것이다.
        Date.getDay() 메서드는 요일을 구하는 메서드로는 이름이 모호하기도 하다. 현재는 사용하지 않는(deprecated) 메서드라서 그나마 다행이다.

     */
    @Before
    public void setUp(){

    }

    @Test
    public void shouldGetDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, 12, 31);
        assertThat(calendar.get(Calendar.YEAR)).isEqualTo(2000);
        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(Calendar.JANUARY);
        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(31);

        /*
            1999년 12월 31일을 지정하려 했으나, 12월의 상수값은 11이므로 직접 숫자 12를 대입하면 2000년 1월 31일로 넘어간다. 숫자 12 대신 11 혹은 Calendar.DECEMBER 상수로 지정해야 1999년 12월 31일이 된다.

            13월을 의미하는 12를 넣어도 Calendar.set() 메서드가 오류를 반환하지 않기 때문에 이런 실수를 인지하기 더욱 어렵다. calendar.setLenient(false)
            메서드를 호출하면 잘못된 월이 지정된 객체에서 IllegalArgumentException을 던져 준다. 그렇게 지정해도 Calendar.set() 메서드가 호출되는 시점이 아니라,
            Calendar.get() 메서드가 호출될 때 Exception이 발생한다는 점도 주의해야한다.
         */
    }

    @Test
    @SuppressWarnings("deprecation")
    public void shouldGetDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2014, Calendar.JANUARY, 1);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        assertThat(dayOfWeek).isEqualTo(Calendar.WEDNESDAY);
        assertThat(dayOfWeek).isEqualTo(4);
        Date theDate = calendar.getTime();
        assertThat(theDate.getDay()).isEqualTo(3);

        /*
            Calendar.get(Calendar.DAY_OF_WEEK) 함수에서 반환한 요일은 int 값으로, 일요일이 1로 표현된다. 따라서 수요일은 4이고, 보통 Calendar.WEDNESDAY 상수와 비교해서 확인한다. 그런데 calendar.getTime() 메서드로 Date 객체를 얻어와서 Date.getDay() 메서드로 요일을 구하면 일요일은 0, 수요일은 3이 된다. 두 개의 클래스 사이에 요일 지정값에 일관성이 없는 것이다.
            Date.getDay() 메서드는 요일을 구하는 메서드로는 이름이 모호하기도 하다. 현재는 사용하지 않는(deprecated) 메서드라서 그나마 다행이다.
         */
    }

    @Test
    public void 자바_캘린더_기본(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1, 0, 0, 0);

        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss.SSS");
        calendar.add(Calendar.DAY_OF_MONTH, 90);
        System.out.println(sdf.format(calendar.getTime()));
    }

    @Test
    public void 조다타임_기본(){
        DateTime dateTime = new DateTime(2018, 4, 13, 0, 0, 0, 0);
        System.out.println(dateTime.plusDays(90).toString("yyyy-MM-dd E HH:mm:ss.SSS"));
    }

    @Test
    public void 패턴비교(){
        DateTime nowTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2018-04-13 00:23:54");
        String result = DateTimeFormat.forPattern("aa").withLocale(new Locale("ko")).print(nowTime);
        assertThat(result).isEqualTo("오전");

        String result2 = DateTimeFormat.forPattern("MM월dd일 HH:mm").withLocale(new Locale("ko")).print(nowTime);
        assertThat(result2).isEqualTo("04월13일 00:23");

        DateTime utcTime = new DateTime(2013, 6, 9, 17, 0, DateTimeZone.UTC);
        DateTime cestTime = new DateTime(2013, 6, 10, 2, 0, DateTimeZone.forID("Asia/Seoul"));

        assertThat(utcTime).as("in UTC time").isEqualTo(cestTime);

    }


    @Test
    public void 시간검증_기본(){
        assertThat(new DateTime("1999-12-30")).isBefore(new DateTime("2000-01-01"));
        assertThat(new DateTime("2000-01-01")).isAfter(new DateTime("1999-12-30"));

        assertThat(new DateTime("2000-01-01")).isBeforeOrEqualTo(new DateTime("2000-01-01"));
        assertThat(new DateTime("2000-01-01")).isAfterOrEqualTo(new DateTime("2000-01-01"));
    }

    @Test
    public void 시간무시(){
        // ... milliseconds
        DateTime dateTime1 = new DateTime(2000, 1, 1, 0, 0, 1, 0, UTC);
        DateTime dateTime2 = new DateTime(2000, 1, 1, 0, 0, 1, 456, UTC);
        assertThat(dateTime1).isEqualToIgnoringMillis(dateTime2);
        // ... seconds
        dateTime1 = new DateTime(2000, 1, 1, 23, 50, 0, 0, UTC);
        dateTime2 = new DateTime(2000, 1, 1, 23, 50, 10, 456, UTC);
        assertThat(dateTime1).isEqualToIgnoringSeconds(dateTime2);
        // ... minutes
        dateTime1 = new DateTime(2000, 1, 1, 23, 50, 0, 0, UTC);
        dateTime2 = new DateTime(2000, 1, 1, 23, 00, 2, 7, UTC);
        assertThat(dateTime1).isEqualToIgnoringMinutes(dateTime2);
        // ... hours
        dateTime1 = new DateTime(2000, 1, 1, 23, 59, 59, 999, UTC);
        dateTime2 = new DateTime(2000, 1, 1, 00, 00, 00, 000, UTC);
        assertThat(dateTime1).isEqualToIgnoringHours(dateTime2);
    }

    @Test
    public void not_in(){

        assertThat(new DateTime("2000-01-01")).isIn(new DateTime("1999-12-22"), new DateTime("2000-01-01")); //A~B까지의 시간안에 포함
        assertThat(new DateTime("2000-01-01")).isNotIn(new DateTime("1999-12-31"), new DateTime("2000-01-02")); //B가 기준 날짜보다 크므로 not in 임
        // same assertions but parameters is String based representation of LocalDateTime
        assertThat(new LocalDateTime("2000-01-01")).isIn("1999-12-31", "2000-01-01").isNotIn("1999-12-31", "2000-01-02");
    }
}
