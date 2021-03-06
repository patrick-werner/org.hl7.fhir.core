package org.hl7.fhir.r4.formats;

/*-
 * #%L
 * org.hl7.fhir.r4
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;

import org.hl7.fhir.utilities.Utilities;

/**
 * A little implementation of a json write to replace Gson .... because Gson screws up decimal values, and *we care*
 * 
 * @author Grahame Grieve
 *
 */
public class JsonCreatorDirect implements JsonCreator {

  private Writer writer;
  private boolean pretty;
  private boolean named;
  private boolean valued;
  private int indent;
  
  public JsonCreatorDirect(Writer writer) {
    super();
    this.writer = writer;
  }

  @Override
  public void setIndent(String indent) {
    this.pretty = !Utilities.noString(indent);
  }

  @Override
  public void beginObject() throws IOException {
    checkState();
    writer.write("{");
    stepIn();
  }

  public void stepIn() throws IOException {
    if (pretty) {
      indent++;
      writer.write("\r\n");
      for (int i = 0; i < indent; i++) {
        writer.write("  ");
      }
    }
  }

  public void stepOut() throws IOException {
    if (pretty) {
      indent--;
      writer.write("\r\n");
      for (int i = 0; i < indent; i++) {
        writer.write("  ");
      }
    }
  }

  private void checkState() throws IOException {
    if (named) {
      if (pretty)
        writer.write(" : ");
      else
        writer.write(":");
      named = false;
    }
    if (valued) {
      writer.write(",");
      if (pretty) {
        writer.write("\r\n");
        for (int i = 0; i < indent; i++) {
          writer.write("  ");
        }        
      }
      valued = false;
    }
  }

  @Override
  public void endObject() throws IOException {
    stepOut();
    writer.write("}");    
  }

  @Override
  public void nullValue() throws IOException {
    checkState();
    writer.write("null");
    valued = true;
  }

  @Override
  public void name(String name) throws IOException {
    checkState();
    writer.write("\""+name+"\"");
    named = true;
  }

  @Override
  public void value(String value) throws IOException {
    checkState();
    writer.write("\""+Utilities.escapeJson(value)+"\"");    
    valued = true;
  }

  @Override
  public void value(Boolean value) throws IOException {
    checkState();
    if (value == null)
      writer.write("null");
    else if (value.booleanValue())
      writer.write("true");
    else
      writer.write("false");
    valued = true;
  }

  @Override
  public void value(BigDecimal value) throws IOException {
    checkState();
    if (value == null)
      writer.write("null");
    else 
      writer.write(value.toString());    
    valued = true;
  }

  @Override
  public void valueNum(String value) throws IOException {
    checkState();
    if (value == null)
      writer.write("null");
    else 
      writer.write(value);    
    valued = true;
  }

  @Override
  public void value(Integer value) throws IOException {
    checkState();
    if (value == null)
      writer.write("null");
    else 
      writer.write(value.toString());    
    valued = true;
  }

  @Override
  public void beginArray() throws IOException {
    checkState();
    writer.write("[");    
  }

  @Override
  public void endArray() throws IOException {
    writer.write("]");        
  }

  @Override
  public void finish() throws IOException {
    // nothing
  }

  @Override
  public void link(String href) {
    // not used
    
  }

}
