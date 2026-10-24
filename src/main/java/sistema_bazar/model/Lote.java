package sistema_bazar.model;

import java.time.LocalDate;

public class Lote {
    private int id;
    private LocalDate dataEntrega;
    private String observacao;
    private String status;
    private int orgaoFiscalizadorId;
    private int orgaoDonatarioId;

    public Lote() {
    }

    public Lote(int id, LocalDate dataEntrega, String observacao,
                String status, int orgaoFiscalizadorId,
                int orgaoDonatarioId) {
        this.id = id;
        this.dataEntrega = dataEntrega;
        this.observacao = observacao;
        this.status = status;
        this.orgaoFiscalizadorId = orgaoFiscalizadorId;
        this.orgaoDonatarioId = orgaoDonatarioId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrgaoFiscalizadorId() {
        return orgaoFiscalizadorId;
    }

    public void setOrgaoFiscalizadorId(int orgaoFiscalizadorId) {
        this.orgaoFiscalizadorId = orgaoFiscalizadorId;
    }

    public int getOrgaoDonatarioId() {
        return orgaoDonatarioId;
    }

    public void setOrgaoDonatarioId(int orgaoDonatarioId) {
        this.orgaoDonatarioId = orgaoDonatarioId;
    }
    }
