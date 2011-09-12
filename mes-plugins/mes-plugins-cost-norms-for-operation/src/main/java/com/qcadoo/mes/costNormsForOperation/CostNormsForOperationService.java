package com.qcadoo.mes.costNormsForOperation;

import static com.google.common.base.Preconditions.checkArgument;
import static com.qcadoo.mes.costNormsForOperation.constants.CostNormsForOperationConstants.FIELDS;
import static com.qcadoo.view.api.ComponentState.MessageType.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qcadoo.localization.api.TranslationService;
import com.qcadoo.mes.technologies.constants.TechnologiesConstants;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.DataDefinitionService;
import com.qcadoo.model.api.Entity;
import com.qcadoo.view.api.ComponentState;
import com.qcadoo.view.api.ViewDefinitionState;
import com.qcadoo.view.api.components.FieldComponent;
import com.qcadoo.view.api.components.FormComponent;

@Service
public class CostNormsForOperationService {

    @Autowired
    private DataDefinitionService dataDefinitionService;

    @Autowired
    private TranslationService translationService;

    /* ****** VIEW EVENT LISTENERS ******* */

    public void copyCostValuesFromOperation(final ViewDefinitionState view, final ComponentState operationLookupState,
            final String[] args) {

        if (operationLookupState.getFieldValue() == null) {
            view.getComponentByReference("form").addMessage(
                    translationService.translate("costNormsForOperation.messages.info.missingOperationReference",
                            view.getLocale()), INFO);
            return;
        }

        Entity operation = dataDefinitionService.get(TechnologiesConstants.PLUGIN_IDENTIFIER,
                TechnologiesConstants.MODEL_OPERATION).get((Long) operationLookupState.getFieldValue());

        applyCostNormsFromGivenSource(view, operation);
    }

    public void copyCostValuesFromTechnology(final ViewDefinitionState view, final ComponentState componentState,
            final String[] args) {

        Entity orderOperationComponent = ((FormComponent) view.getComponentByReference("form")).getEntity();

        // Be sure that entity isn't in detached state
        orderOperationComponent = orderOperationComponent.getDataDefinition().get(orderOperationComponent.getId());

        applyCostNormsFromGivenSource(view, orderOperationComponent.getBelongsToField("technologyOperationComponent"));

    }

    public void inheritOperationNormValues(final ViewDefinitionState view, final ComponentState componentState,
            final String[] args) {
        copyCostValuesFromOperation(view, componentState, args);
    }

    private void applyCostNormsFromGivenSource(final ViewDefinitionState view, final Entity source) {
        checkArgument(source != null, "source entity is null");
        FieldComponent component = null;

        for (String fieldName : FIELDS) {
            component = (FieldComponent) view.getComponentByReference(fieldName);
            component.setFieldValue(source.getField(fieldName));
        }

        // FIXME MAKU - double notification after change operation lookup value
        // view.getComponentByReference("form").addMessage(translationService.translate("costNormsForOperation.messages.success.copyCostNormsSuccess",
        // view.getLocale()), SUCCESS);
    }

    /* ******* MODEL HOOKS ******* */

    public void copyCostNormsToOrderOperationComponent(final DataDefinition dd, final Entity orderOperationComponent) {
        Entity source = orderOperationComponent.getBelongsToField("technologyOperationComponent");
        copyCostValuesFromGivenOperation(orderOperationComponent, source);
    }

    public void copyCostNormsToTechnologyOperationComponent(final DataDefinition dd, final Entity orderOperationComponent) {
        Entity source = orderOperationComponent.getBelongsToField("operation");
        copyCostValuesFromGivenOperation(orderOperationComponent, source);
    }

    /* ******* CUSTOM HELPER(S) ******* */

    private void copyCostValuesFromGivenOperation(final Entity target, final Entity maybeDetachedSource) {
        checkArgument(target != null, "given target is null");
        checkArgument(maybeDetachedSource != null, "given target is null");

        // IMPORTANT! be sure that entity isn't in detached state
        Entity source = maybeDetachedSource.getDataDefinition().get(maybeDetachedSource.getId());

        for (String fieldName : FIELDS) {
            if (source.getField(fieldName) == null) {
                continue;
            }
            target.setField(fieldName, source.getField(fieldName));
        }
    }
}
