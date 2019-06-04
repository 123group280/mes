/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo MES
 * Version: 1.4
 *
 * This file is part of Qcadoo.
 *
 * Qcadoo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */
package com.qcadoo.mes.operationalTasks;

import java.util.List;

import com.qcadoo.model.api.Entity;

public interface OperationalTasksService {

    /**
     * Gets technology operation components for operation
     * 
     * @param operation
     *            operation
     * 
     * @return technologyOperationComponents
     */
    List<Entity> getTechnologyOperationComponentsForOperation(final Entity operation);

    /**
     * Gets operational tasks for order
     * 
     * @param order
     *            order
     * 
     * @return operationalTasks
     */
    List<Entity> getOperationalTasksForOrder(final Entity order);

    /**
     * Is operational task type task other case
     * 
     * @param type
     * 
     * @return boolean
     */
    boolean isOperationalTaskTypeOtherCase(final String type);

    /**
     * Is operational task type task execution operation in order
     * 
     * @param type
     * 
     * @return boolean
     */
    boolean isOperationalTaskTypeExecutionOperationInOrder(final String type);

    Entity findOperationalTasks(Entity order, Entity toc);
}
