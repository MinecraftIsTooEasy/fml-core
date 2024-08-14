/*
 * Forge Mod Loader
 * Copyright (c) 2012-2013 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     cpw - implementation
 */

package cpw.mods.fml.common.asm.transformers.deobf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.io.Closer;
import com.google.common.io.InputSupplier;
import net.contrapunctus.lzma.LzmaInputStream;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class LZMAInputSupplier implements InputSupplier<InputStream>  {
    private InputStream compressedData;

    public LZMAInputSupplier(InputStream compressedData)
    {
        this.compressedData = compressedData;
    }

    @Override
    public InputStream getInput() {
        return new LzmaInputStream(this.compressedData);
    }


}
