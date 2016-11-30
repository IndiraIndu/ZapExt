/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2012 The ZAP Development team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.zaproxy.zap.extension.anticsrf;

import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.zaproxy.zap.extension.api.API;
import org.zaproxy.zap.utils.DesktopUtils;
import org.zaproxy.zap.view.popup.PopupMenuItemHistoryReferenceContainer;

public class PopupMenuGenerateForm extends PopupMenuItemHistoryReferenceContainer {

	private static final long serialVersionUID = 1L;
	
    /**
     * @param label
     */
    public PopupMenuGenerateForm(String label) {
    	super(label);
    }

	@Override
	public void performAction(HistoryReference href) {
		// Redirect to the form generated by the API
		DesktopUtils.openUrlInBrowser(AntiCsrfAPI.getAntiCsrfFormUrl(href.getHistoryId()));
	}

	@Override
    public boolean isButtonEnabledForHistoryReference (HistoryReference href) {
		if (API.getInstance().isEnabled() && DesktopUtils.canOpenUrlInBrowser()) {
			try {
				if (HttpRequestHeader.POST.equals(href.getMethod()) && href.getRequestBodyLength() > 0) {
					return true;
				}
			} catch (Exception e) {
				// Ignore - this is 'just' for a right click menu
			}
		}
		return false;
    }

}
