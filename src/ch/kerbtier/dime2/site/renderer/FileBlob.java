package ch.kerbtier.dime2.site.renderer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

import ch.kerbtier.helene.HBlob;
import ch.kerbtier.helene.store.mod.ByteBufferHBlob;

public class FileBlob implements HBlob {

  private ByteBufferHBlob subject = new ByteBufferHBlob();

  public FileBlob(Path p) {
    try {
      byte[] data = FileUtils.readFileToByteArray(p.toFile());
      subject.setData(ByteBuffer.wrap(data));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public byte[] asArray() {
    return subject.asArray();
  }

  @Override
  public ByteBuffer asBuffer() {
    return subject.asBuffer();
  }

  @Override
  public String getHash() {
    return subject.getHash();
  }
}
