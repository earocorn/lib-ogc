/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import org.junit.Test;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.swe.SWEHelper;
import org.vast.swe.fast.TextDataParser;
import org.vast.swe.fast.TextDataWriter;
import org.vast.util.DateTimeFormat;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;


public class TestTextDataParser
{

    @Test
    public void test() throws IOException
    {
        SWEHelper fac = new SWEHelper();
        int arraySize = 5;

        DataArray array = fac.createArray()
            .withFixedSize(arraySize)
            .name("array")
            .withElement("elt", fac.createRecord()
                .addSamplingTimeIsoUTC("f0")
                .addField("f1", fac.createQuantity().build())
                .addField("f2", fac.createQuantity().build())
                .addField("rec2", fac.createRecord()
                    .addField("f3", fac.createCount().build())
                    .addField("f4", fac.createQuantity().build())
                    .build())
                .build())
            .build();

        // generate data
        String tokenSep = "!";
        String blockSep = "@@\n";
        DateTimeFormat timeFormat = new DateTimeFormat();
        double now = new Date().getTime()/1000.;
        StringBuilder buf = new StringBuilder();
        for (int r=0; r<100; r++)
        {
            for (int i=0; i<arraySize; i++)
            {
                double val = (double)i + r*10;
                buf.append(timeFormat.formatIso(now+val, 0)).append(tokenSep)
                   .append(val/1000.).append(tokenSep)
                   .append(val+100).append(tokenSep)
                   .append(((int)val)+200).append(tokenSep)
                   .append(val+300);//.append(tokenSep)
                   //.append("f5").append(tokenSep)
                   //.append("test" + i);
                if (i == arraySize-1)
                    buf.append(blockSep);
                else
                    buf.append(tokenSep);
            }
        }
        //System.out.println(buf.toString());

        /*for (int it=0; it<10; it++)
        {
            ByteArrayInputStream is = new ByteArrayInputStream(buf.toString().getBytes());

            // init & launch parser
            //DataStreamParser parser = new AsciiDataParser();
            DataStreamParser parser = new TextDataParser();
            parser.setDataComponents(array);
            parser.setDataEncoding(fac.newTextEncoding(tokenSep, blockSep));
            parser.setInput(is);
            parser.setRenewDataBlock(false);

            long t0 = System.currentTimeMillis();
            DataBlock dataBlk;
            do
            {
                //System.out.println();
                dataBlk = parser.parseNextBlock();
                //if (dataBlk != null)
                {
                //    for (int i=0; i<dataBlk.getAtomCount();i++)
                //    System.out.print(dataBlk.getStringValue(i) + ",");
                }
            }
            while (dataBlk != null);
            System.out.println("Exec Time = " + (System.currentTimeMillis()-t0));
        }*/

        for (int it=0; it<10; it++)
        {
            ByteArrayInputStream is = new ByteArrayInputStream(buf.toString().getBytes());

            // init parser
            DataStreamParser parser = new TextDataParser();
            parser.setDataComponents(array);
            parser.setDataEncoding(fac.newTextEncoding(tokenSep, blockSep));
            parser.setInput(is);
            parser.setRenewDataBlock(false);

            // init writer
            DataStreamWriter writer = new TextDataWriter();
            writer.setDataComponents(array);
            writer.setDataEncoding(fac.newTextEncoding(tokenSep, blockSep));
            /*DataStreamWriter writer = new JsonDataWriter();
            writer.setDataComponents(array);
            writer.setDataEncoding(fac.newXMLEncoding());*/
            //writer.setOutput(System.out);
            writer.setOutput(new ByteArrayOutputStream(1024*1024));

            long t0 = System.currentTimeMillis();
            DataBlock dataBlk;
            do
            {
                dataBlk = parser.parseNextBlock();
                if (dataBlk != null)
                {
                    writer.write(dataBlk);
                    writer.flush();
                }
            }
            while (dataBlk != null);

            System.out.println("Exec Time = " + (System.currentTimeMillis()-t0));
        }
    }

}
