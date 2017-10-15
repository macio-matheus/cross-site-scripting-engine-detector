package utilsLib.util;

import java.util.ArrayList;

public class ConsultInfo {
    private ArrayList consultComponents;
    private String description;

    public ConsultInfo() {
        this.consultComponents = new ArrayList();
    }

    public void addConsultComponent(ConsultComponent consultComponent) {
        if (consultComponent == null) {
            throw new IllegalArgumentException(
                    "Parametro invalido: consultComponent");
        }
        this.consultComponents.add(consultComponent);
    }

    public boolean removeConsultComponent(ConsultComponent consultComponent) {
        if (consultComponent == null) {
            throw new IllegalArgumentException(
                    "Parametro invalido: consultComponent");
        }
        return this.consultComponents.remove(consultComponent);
    }

    public ConsultComponent[] getConsultComponents() {
        ConsultComponent[] cc = new ConsultComponent[consultComponents.size()];
        return (ConsultComponent[])this.consultComponents.toArray(cc);
    }

    public int getSize() {
        return this.consultComponents.size();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(ConsultInfo ci) {
        ConsultComponent[] cc1 = this.getConsultComponents();
        ConsultComponent[] cc2 = ci.getConsultComponents();

        if (cc1.length != cc2.length) {
            return false;
        }

        for (int i = 0; i < cc1.length; i++) {
            if (!cc1[i].equals(cc2[i])) {
                return false;
            }
        }

        return true;
    }

}
