package ch.kerbtier.dime2.admin.model;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.parboiled.common.Base64;

import ch.kerbtier.dime2.admin.model.form.FormEntity;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.helene.HBlob;
import ch.kerbtier.webb.di.InjectSingleton;
import ch.kerbtier.webb.util.ContextInfo;

@Inject
public class FileInput extends FormElement {
  
  @InjectSingleton
  private ContextInfo contextInfo;

  @ADHS
  private String url;
  
  @ADHS
  private String previewURL = null;
  
  @ADHS
  private String extension = null;
  
  private ByteBuffer data;

  public FileInput(String field) {
    super(field);
  }

  public void setValue(String value) {
    Pattern pattern = Pattern.compile("^data:([a-z]+/[a-z]+);base64,");
    Matcher matcher = pattern.matcher(value);
    
    if(matcher.find()) {
      value = value.substring(matcher.end());
    }
    
    byte[] bd = Base64.rfc2045().decode(value);
    ByteBuffer bb = ByteBuffer.wrap(bd);
    getForm().update(getField(), bb);
}

  @Override
  public void initValue(FormEntity subject) {
    Object bd = subject.get(getField());
    if(bd instanceof HBlob) {
      setData(((HBlob) bd).asBuffer());
    }
  }

  public void setData(ByteBuffer data) {
    this.data = data;
    this.url = contextInfo.getPath() + "/admin/image/" + getId();
  }

  public ByteBuffer getData() {
    return data;
  }
}
