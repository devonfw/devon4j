package com.devonfw.module.security.common.base.accesscontrol;

import java.io.InputStream;
import java.io.OutputStream;

import com.devonfw.module.security.common.api.accesscontrol.AccessControlSchema;

/**
 * This is the interface to {@link #read(InputStream)} and {@link #write(AccessControlSchema, OutputStream)} the
 * {@link AccessControlSchema}.
 *
 */
public interface AccessControlSchemaMapper {

  /**
   * Reads the {@link AccessControlSchema} from the given {@link InputStream}.
   *
   * @param in is the {@link InputStream} with {@link AccessControlSchema} to read. Has to be
   *        {@link InputStream#close() closed} by the caller of this method who created the stream.
   * @return the {@link AccessControlSchema} represented by the given input.
   */
  AccessControlSchema read(InputStream in);

  /**
   * Writes the given {@link AccessControlSchema} to the given {@link OutputStream}.
   *
   * @param conf is the {@link AccessControlSchema} to write.
   * @param out is the {@link OutputStream} where to write the {@link AccessControlSchema} to. Has to be
   *        {@link OutputStream#close() closed} by the caller of this method who created the stream.
   */
  void write(AccessControlSchema conf, OutputStream out);

}
