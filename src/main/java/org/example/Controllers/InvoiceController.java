package org.example.Controllers;

import org.example.Models.Invoice;
import org.example.Models.InvoiceDetail;
import org.example.Services.InvoiceService;

import java.util.Date;
import java.util.List;

public class InvoiceController {
    private InvoiceService service = new InvoiceService();

    public List<Invoice> getAllInvoices() {
        return service.getAllInvoices();
    }

    public Invoice getInvoiceById(int id) {
        return service.getInvoiceById(id);
    }

    public List<InvoiceDetail> getDetailsByInvoiceId(int invoiceId) {
        return service.getDetailsByInvoiceId(invoiceId);
    }

    public List<Invoice> searchInvoices(String keyword, Date from, Date to) {
        return service.searchInvoices( keyword,  from, to);
    }

}
