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

package cpw.mods.fml.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.asm.ASMTransformer;
import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.common.asm.transformers.ModAPITransformer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.modloader.BaseModProxy;
import net.xiaoyu233.fml.classloading.KnotClassLoader;

/**
 * A simple delegating class loader used to load mods into the system
 *
 *
 * @author cpw
 *
 */
public class ModClassLoader extends KnotClassLoader
{
    private static final List<String> STANDARD_LIBRARIES = ImmutableList.of("jinput.jar", "lwjgl.jar", "lwjgl_util.jar");

    public ModClassLoader() {
    }

    public void addFile(File modFile) throws MalformedURLException
    {
        URL url = modFile.toURI().toURL();
        super.addURL(url);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
        return super.loadClass(name);
    }

    public File[] getParentSources() {
        List<URL> urls=super.getSources();
        File[] sources=new File[urls.size()];
        try
        {
            for (int i = 0; i<urls.size(); i++)
            {
                sources[i]=new File(urls.get(i).toURI());
            }
            return sources;
        }
        catch (URISyntaxException e)
        {
            FMLLog.log(Level.SEVERE, e, "Unable to process our input to locate the minecraft code");
            throw new LoaderException(e);
        }
    }

    public List<String> getDefaultLibraries()
    {
        return STANDARD_LIBRARIES;
    }

    public Class<? extends BaseModProxy> loadBaseModClass(String modClazzName) throws Exception
    {
        AccessTransformer accessTransformer = null;
        for (IClassTransformer transformer : super.getTransformers())
        {
            if (transformer instanceof AccessTransformer)
            {
                accessTransformer = (AccessTransformer) transformer;
                break;
            }
        }
        if (accessTransformer == null)
        {
            FMLLog.log(Level.SEVERE, "No access transformer found");
            throw new LoaderException();
        }
        accessTransformer.ensurePublicAccessFor(modClazzName);
        return (Class<? extends BaseModProxy>) Class.forName(modClazzName, true, this);
    }

    public void clearNegativeCacheFor(Set<String> classList)
    {
        super.clearNegativeEntries(classList);
    }

    public ModAPITransformer addModAPITransformer(ASMDataTable dataTable)
    {
        super.registerTransformer("cpw.mods.fml.common.asm.transformers.ModAPITransformer");
        List<IClassTransformer> transformers = super.getTransformers();
        ModAPITransformer modAPI = (ModAPITransformer) transformers.get(transformers.size()-1);
        modAPI.initTable(dataTable);
        return modAPI;
    }
}
