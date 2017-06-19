// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.simplefileio.runtime;

import org.talend.components.simplefileio.runtime.ugi.UgiDoAs;

public abstract class SimpleRecordFormatBase implements SimpleRecordFormat {

    static {
        // Ensure that the singleton for the SimpleFileIOAvroRegistry is created.
        SimpleFileIOAvroRegistry.get();
    }

    protected final UgiDoAs doAs;

    protected final String path;

    protected final boolean overwrite;

    protected final int limit;

    protected final boolean mergeOutput;

    private final ExtraHadoopConfiguration extraConfig = new ExtraHadoopConfiguration();

    public SimpleRecordFormatBase(UgiDoAs doAs, String path, boolean overwrite, int limit, boolean mergeOutput) {
        this.doAs = doAs;
        this.path = path;
        this.overwrite = overwrite;
        this.limit = limit;
        this.mergeOutput = mergeOutput;
    }

    /**
     * Return the last {@link ExtraHadoopConfiguration} used for a generated runtime for the record format.
     */
    public ExtraHadoopConfiguration getExtraHadoopConfiguration() {
        return extraConfig;
    }
}
