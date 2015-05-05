package ch.kerbtier.dime2.site.renderer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class FormatHelper implements Helper<Object> {
  
  private static SimpleDateFormat rfc822 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

  @Override
  public CharSequence apply(Object o, Options args) throws IOException {
    
    if(o instanceof Date) {
      return formatDate((Date)o, (String)args.param(0));
    }
    
    return "[invalid value]";
  }

  public String formatDate(Date value, String format) {
    // Hardcode UK locale for dateformats.
    // All other formats are just weird.
    if("short".equals(format)) {
      return DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(value);
    } else if("medium".equals(format)) {
      return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK).format(value);
    } else if("rfc822".equals(format)) {
      return rfc822.format(value);
    } else {
      return DateFormat.getDateInstance(DateFormat.LONG, Locale.UK).format(value);
    }
  }

}
