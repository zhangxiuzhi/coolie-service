package zjtech.coolie.handler.appcms;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * * URL - Uniform Resource Locator (Immutable, ThreadSafe)
 * <p>
 * url example:
 * <ul>
 * <li>http://www.facebook.com/friends?param1=value1&amp;param2=value2
 * <li>http://username:password@10.20.130.230:8080/list?version=1.0.0
 * <li>ftp://username:password@192.168.1.7:21/1/read.txt
 * <li>registry://192.168.1.7:9090/com.alibaba.service1?param1=value1&amp;param2=value2
 * </ul>
 * <p>
 * Some strange example below:
 * <ul>
 * <li>192.168.1.3:20880<br>
 * for this case, url protocol = null, url host = 192.168.1.3, port = 20880, url path = null
 * <li>file:///home/user1/router.js?type=script<br>
 * for this case, url protocol = null, url host = null, url path = home/user1/router.js
 * <li>file://home/user1/router.js?type=script<br>
 * for this case, url protocol = file, url host = home, url path = user1/router.js
 * <li>file:///D:/1/router.js?type=script<br>
 * for this case, url protocol = file, url host = null, url path = D:/1/router.js
 * <li>file:/D:/1/router.js?type=script<br>
 * same as above file:///D:/1/router.js?type=script
 * <li>/home/user1/router.js?type=script <br>
 * for this case, url protocol = null, url host = null, url path = home/user1/router.js
 * <li>home/user1/router.js?type=script <br>
 * for this case, url protocol = null, url host = home, url path = user1/router.js
 * </ul>
 */
public class URLParserTest {

  /**
   * http://www.facebook.com/friends?param1=value1&amp;param2=value2
   */
  static Pattern pt = Pattern.compile("(\\w)*?:/{1,2}([^\\?]+)?(?:\\?{1})(([a-z0-9]+=[a-z0-9]+)?(&[a-z0-9]+=[a-z0-9])?)*");
  static Pattern pt2 = Pattern.compile("((\\w+)(?=:{1}))(?:\\/{1,2}){0,1}([^\\?]+)\\?{0,1}(.*)");
  static Pattern pt3 = Pattern.compile("(\\w*):?(?:\\/{1,2})([^\\?]+)\\?(.*)");

  public static void main(String[] args) {
    filter("http://www.hhh.com/friends?param1=vRRRalue1&ssdf_sdw=43232", "f1");
    filter("file:///D:/1/router.js?type=script&param2=value2&ssdf_sdw=43232", "f2");
    filter("http://username:password@10.20.130.230:8080/list?version=1.0.0", "f3");
    filter("registry://192.168.1.7:9090/com.ttt.service1?param1=value1&amp;param2=value2", "f4");
    filter("192.168.1.3:20880", "f5");

//    noCaptureGroup();
  }

  public static void filter(String args, String key) {

    Matcher mt = pt3.matcher(args);
    int j = 0;
    while (mt.find()) {
      System.out.println(key + "-" + j++ + "," + mt.group());
    }
   /* if (mt.matches()) {
      for (int i = 1; i <= mt.groupCount(); i++) {
        System.out.println(key + " " + i + ":" + mt.group(i));
      }
    }  */
  }

  public static void noCaptureGroup() {
    System.out.println();
    Pattern pattern = Pattern.compile("(?:(\\d+))?\\s?([a-zA-Z]+)?.+");
//     pattern = pt;
    String source = "2133 fdsdee4333";
    Matcher matcher = pattern.matcher(source);
    if (matcher.matches()) {
      for (int i = 0; i <= matcher.groupCount(); i++) {
        System.out.println("group " + i + ":" + matcher.group(i));
      }
    }
  }

  static {
    pt2 = pt3;

  }
}