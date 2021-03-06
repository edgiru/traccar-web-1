/*
 * Copyright 2015 Vitaly Litvak (vitavaque@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.web.server.model;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.persist.Transactional;
import org.traccar.web.client.model.PicturesService;
import org.traccar.web.shared.model.DeviceIcon;
import org.traccar.web.shared.model.Picture;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;

@Singleton
public class PicturesServiceImpl extends RemoteServiceServlet implements PicturesService {
    @Inject
    private Provider<EntityManager> entityManager;

    @Transactional
    @RequireUser
    @Override
    public List<DeviceIcon> getMarkerPictures() {
        return entityManager.get().createQuery("SELECT i FROM DeviceIcon i ORDER BY i.id DESC", DeviceIcon.class)
                .getResultList();
    }

    @Transactional
    @RequireUser
    @Override
    public DeviceIcon addMarkerPicture(DeviceIcon marker) {
        if (marker.getDefaultIcon() != null) {
            marker.setDefaultIcon(entityManager.get().find(Picture.class, marker.getDefaultIcon().getId()));
        }
        if (marker.getSelectedIcon() != null) {
            marker.setSelectedIcon(entityManager.get().find(Picture.class, marker.getSelectedIcon().getId()));
        }
        if (marker.getOfflineIcon() != null) {
            marker.setOfflineIcon(entityManager.get().find(Picture.class, marker.getOfflineIcon().getId()));
        }
        entityManager.get().persist(marker);
        return marker;
    }
}
