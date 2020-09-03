package edu.upc.essi.catalog.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class TabbedPrintStream extends PrintStream {

	public TabbedPrintStream(OutputStream out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	public TabbedPrintStream(String fileName) throws FileNotFoundException {
		super(fileName);
		// TODO Auto-generated constructor stub
	}

	public TabbedPrintStream(File file) throws FileNotFoundException {
		super(file);
		// TODO Auto-generated constructor stub
	}

	public TabbedPrintStream(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
		// TODO Auto-generated constructor stub
	}

	public TabbedPrintStream(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		// TODO Auto-generated constructor stub
	}

	public TabbedPrintStream(String fileName, Charset charset) throws IOException {
		super(fileName, charset);
		// TODO Auto-generated constructor stub
	}

	public TabbedPrintStream(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
		// TODO Auto-generated constructor stub
	}

	public TabbedPrintStream(File file, Charset charset) throws IOException {
		super(file, charset);
		// TODO Auto-generated constructor stub
	}

	public TabbedPrintStream(OutputStream out, boolean autoFlush, String encoding) throws UnsupportedEncodingException {
		super(out, autoFlush, encoding);
		// TODO Auto-generated constructor stub
	}

	public TabbedPrintStream(OutputStream out, boolean autoFlush, Charset charset) {
		super(out, autoFlush, charset);
		// TODO Auto-generated constructor stub
	}

	public void println(int tabs, String s) {
		for (int i = 0; i < tabs; i++) {
			super.print("\t");
		}
		super.println(s);
	}
	public void print(int tabs, String s) {
		for (int i = 0; i < tabs; i++) {
			super.print("\t");
		}
		super.print(s);
	}
}
