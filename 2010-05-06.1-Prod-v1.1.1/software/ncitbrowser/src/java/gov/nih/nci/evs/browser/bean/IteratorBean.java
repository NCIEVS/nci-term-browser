package gov.nih.nci.evs.browser.bean;


import java.io.*;
import java.util.*;

import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.concepts.Concept;

import gov.nih.nci.evs.browser.common.Constants;
import gov.nih.nci.evs.browser.properties.NCItBrowserProperties;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction with the National Cancer Institute,
 * and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
 * "This product includes software developed by NGIT and the National Cancer Institute."
 * If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
 * wherever such third-party acknowledgments normally appear.
 * 3. The names "The National Cancer Institute", "NCI" and "NGIT" must not be used to endorse or promote products derived from this software.
 * 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
 * the recipient to use any trademarks owned by either NCI or NGIT
 * 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 * NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 * Modification history
 *     Initial implementation kim.ong@ngc.com
 *
 */

public class IteratorBean extends Object {
    static int DEFAULT_MAX_RETURN = 100;
    ResolvedConceptReferencesIterator iterator = null;
    int size = 0;
    List list = null;

    int pageNumber;
    int pageSize;
    int startIndex;
    int endIndex;
    int numberOfPages;

    int lastResolved;
    int maxReturn = 100;
    String message = null;

    String key = null;
    boolean timeout = false;

	public IteratorBean(ResolvedConceptReferencesIterator iterator) {
		this.iterator = iterator;
		this.maxReturn = DEFAULT_MAX_RETURN;
		initialize();
	}


    public IteratorBean(ResolvedConceptReferencesIterator iterator, int maxReturn) {
		this.iterator = iterator;
		this.maxReturn = maxReturn;
		initialize();
	}

	public int getNumberOfPages() {
		return this.numberOfPages;
	}


    public void setIterator(ResolvedConceptReferencesIterator iterator) {
		this.iterator = iterator;
		this.maxReturn = DEFAULT_MAX_RETURN;
		initialize();
	}

    public ResolvedConceptReferencesIterator getIterator() {
		return this.iterator;
	}

	public boolean getTimeout() {
		return timeout;
	}

	public void initialize() {
		try {
			if (iterator == null) {
				this.size = 0;
			} else {
				this.size = iterator.numberRemaining();
		    }
			this.pageNumber = 1;
			this.list = new ArrayList(size);
			for (int i=0; i<size; i++) {
			    list.add(null);
			}
			this.pageSize = Constants.DEFAULT_PAGE_SIZE;
			this.numberOfPages = size / pageSize;
			if (this.pageSize * this.numberOfPages < size) {
				this.numberOfPages = this.numberOfPages + 1;
			}
			this.lastResolved = -1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public int getMumberOfPages() {
		return this.numberOfPages;
	}

	public int getSize() {
		return this.size;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public int getLastResolved() {
		return this.lastResolved;
	}

	public int getStartIndex(int pageNumber) {
		startIndex = (pageNumber-1) * pageSize;
		if (startIndex < 0) startIndex = 0;
		return startIndex;
	}


	public int getEndIndex(int pageNumber) {
		endIndex = pageNumber * pageSize - 1;
		if (endIndex > (size-1)) endIndex = size-1;
		return endIndex;
	}


	public List getData(int pageNumber) {
		int idx1 = getStartIndex(pageNumber);
		int idx2 = getEndIndex(pageNumber);
		return getData(idx1, idx2);
	}


	public List getData(int idx1, int idx2) {
		System.out.println("Retrieving data (from: " + idx1 + " to: " + idx2 + ")");
		long ms = System.currentTimeMillis();
		long dt = 0;
		long total_delay = 0;
		timeout = false;
        try {
			while(iterator != null && iterator.hasNext() && lastResolved < idx2) {
				ResolvedConceptReference[] refs = iterator.next(maxReturn).getResolvedConceptReference();
				for(ResolvedConceptReference ref : refs) {
					//displayRef(ref);
					lastResolved++;
					this.list.set(lastResolved, ref);
				}
				dt = System.currentTimeMillis() - ms;
				ms = System.currentTimeMillis();
				total_delay = total_delay + dt;
				if (total_delay > NCItBrowserProperties.getPaginationTimeOut() * 60 * 1000) {
					timeout = true;
					System.out.println("Time out at: " + lastResolved);
					break;
				}
			}

		} catch (Exception ex) {
			//ex.printStackTrace();
		}


		/*
		for (int i=idx1; i<=idx2; i++) {
			ResolvedConceptReference rcr = (ResolvedConceptReference) this.list.get(i);
			rcr_list.add(rcr);
			if (i > lastResolved) break;
		}
		*/

		Vector temp_vec = new Vector();
		for (int i=idx1; i<=idx2; i++) {
			ResolvedConceptReference rcr = (ResolvedConceptReference) this.list.get(i);
			temp_vec.add(rcr);
			if (i > lastResolved) break;
		}
		List rcr_list = new ArrayList();
		for (int i=0; i<temp_vec.size(); i++) {
			rcr_list.add(null);
		}

		for (int i=0; i<temp_vec.size(); i++) {
			ResolvedConceptReference rcr = (ResolvedConceptReference) temp_vec.elementAt(i);
			rcr_list.set(i, rcr);
		}

		System.out.println("getData Run time (ms): "
					+ (System.currentTimeMillis() - ms));
		return rcr_list;
	}


	protected void displayRef(ResolvedConceptReference ref){
		System.out.println(ref.getConceptCode() + ":" + ref.getEntityDescription().getContent());
	}

	protected void displayRef(int k, ResolvedConceptReference ref){
		System.out.println("(" + k + ") " + ref.getCodingSchemeName() + " " + ref.getConceptCode() + ":" + ref.getEntityDescription().getContent());
	}

	protected void displayRef(OutputStreamWriter osWriter, int k, ResolvedConceptReference ref){
		try {
			osWriter.write("(" + k + ") " + ref.getConceptCode() + ":" + ref.getEntityDescription().getContent() + "\n");
		} catch (Exception ex) {

		}
	}

	public void dumpData(List list) {
		if (list == null) {
			System.out.println("WARNING: dumpData list = null???");
			return;
		}
		for (int i=0; i<list.size(); i++) {
			ResolvedConceptReference rcr = (ResolvedConceptReference) list.get(i);
			int j = i+1;
			displayRef(j, rcr);
		}
	}

	public void dumpData(OutputStreamWriter osWriter, List list) {
		if (list == null) {
			System.out.println("WARNING: dumpData list = null???");
			return;
		}
		for (int i=0; i<list.size(); i++) {
			ResolvedConceptReference rcr = (ResolvedConceptReference) list.get(i);
			int j = i+1;
			displayRef(osWriter, j, rcr);
		}
	}

    public void setKey(String key) {
		this.key = key;
	}


    public String getKey() {
		return this.key;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}