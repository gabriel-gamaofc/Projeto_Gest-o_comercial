/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import java.util.HashMap;
import static javassist.CtMethod.ConstParameter.integer;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.util.HashMap;

public class TelaOs extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
// a linha abaixo cria  uma variavel para armezar um texto da radiobutton

    private String tipo;

    /**
     * Creates new form TelaOs
     */
    public TelaOs() {
        initComponents();
        conexao = ModuloConexao.conector();
        popular();
    }

    private void popular() {
        String atu = "select idcli as Id,nomecli as Nome,fonecli as Fone, CPF from tbclientes ";
        try {
            pst = conexao.prepareStatement(atu);
            rs = pst.executeQuery();
            tblOsCli.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void pesquisar_cliente() {
        String sql = "select idcli as Id,nomecli as Nome,fonecli as Fone, CPF from tbclientes where nomecli like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtOsPesquisar.getText() + "%");
            rs = pst.executeQuery();
            tblOsCli.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setar_campos() {
        int setar = tblOsCli.getSelectedRow();
        txtOsId.setText(tblOsCli.getModel().getValueAt(setar, 0).toString());
        txtOsCpf.setText(tblOsCli.getModel().getValueAt(setar, 3).toString());

        // as linhas abaixos permitem clicar num cliente da tabela e com isso ele vai por os dados de os do cliente nas labels 
        String num_os = txtOsCpf.getText();
        String sql = "select * from tbos where CPF = " + num_os;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtOsNº.setText(rs.getString(1));
                txtOsData.setText(rs.getString(2));
                //setando ps radioButtom
                String rbtTipo = rs.getString(3);
                if (rbtTipo.equals("Ordem de serviço")) {
                    rbtnOsOrdem.setSelected(true);
                    tipo = "Ordem de serviço";
                } else {
                    rbtntOsOrca.setSelected(true);
                    tipo = "Orçamento";
                }
                cbxOsSit.setSelectedItem(rs.getString(4));
                txtOsEquip.setText(rs.getString(5));
                txtOsDef.setText(rs.getString(6));
                txtOsServ.setText(rs.getString(7));
                txtOsTec.setText(rs.getString(8));
                txtOsVal.setText(rs.getString(9));
                txtOsId.setText(rs.getString(10));
                txtOsCpf.setText(rs.getString(11));
                //evitando problemas
                desabilitação();
            } else {
                JOptionPane.showMessageDialog(null, "Não possui Os cadastrado");
            }

        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);

        }

    }

    private void emitir_os() {
        String sql = "insert into tbos (tipo, situacao,equipamento,defeito,servico,tecnico,valor,idcli,CPF) Values(?,?,?,?,?,?,?,?,?)";
        try {
            conexao = ModuloConexao.conector();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cbxOsSit.getSelectedItem().toString());
            pst.setString(3, txtOsEquip.getText());
            pst.setString(4, txtOsDef.getText());
            pst.setString(5, txtOsServ.getText());
            pst.setString(6, txtOsTec.getText());
            //.replace substitui a virgula pelo ponto.
            pst.setString(7, txtOsVal.getText().replace(",", "."));
            pst.setString(8, txtOsId.getText());
            pst.setString(9, txtOsCpf.getText());

            //validação
            if (((((((txtOsId.getText().isEmpty() || txtOsEquip.getText().isEmpty() || txtOsDef.getText().isEmpty() || txtOsServ.getText().isEmpty() || txtOsTec.getText().isEmpty() || txtOsVal.getText().isEmpty() || txtOsCpf.getText().isEmpty()))))))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos");
            } else {
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso");
                    limpar();

                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            //System.out.println(e);
        }
    }

    private void limpar() {
        txtOsEquip.setText(null);
        txtOsDef.setText(null);
        txtOsServ.setText(null);
        txtOsTec.setText(null);
        txtOsVal.setText(null);
        txtOsId.setText(null);
        txtOsNº.setText(null);
        txtOsData.setText(null);
        txtOsCpf.setText(null);
    }

    //Metódo para pesquisar
    private void pesquisar_os() {

        String num_os = JOptionPane.showInputDialog("Número do CPF");
        String sql = "select * from tbos where CPF = " + num_os;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtOsNº.setText(rs.getString(1));
                txtOsData.setText(rs.getString(2));
                //setando ps radioButtom
                String rbtTipo = rs.getString(3);
                if (rbtTipo.equals("Ordem de serviço")) {
                    rbtnOsOrdem.setSelected(true);
                    tipo = "Ordem de serviço";
                } else {
                    rbtntOsOrca.setSelected(true);
                    tipo = "Orçamento";
                }
                cbxOsSit.setSelectedItem(rs.getString(4));
                txtOsEquip.setText(rs.getString(5));
                txtOsDef.setText(rs.getString(6));
                txtOsServ.setText(rs.getString(7));
                txtOsTec.setText(rs.getString(8));
                txtOsVal.setText(rs.getString(9));
                txtOsId.setText(rs.getString(10));
                txtOsCpf.setText(rs.getString(11));
                //evitando problemas
                desabilitação();
            } else {
                JOptionPane.showMessageDialog(null, "Os não cadastrado");
            }

        } catch (java.sql.SQLSyntaxErrorException e) {
            JOptionPane.showMessageDialog(null, "Os invalida");
            // System.out.println(e);

        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);

        }
    }

    // o metodo abaixo desabilita a opção de cirar barra de pesquia e a tabela clientes dainte da operação de consultar os para evitar problemas
    private void desabilitação() {
        btbOsCreate.setEnabled(false);
        txtOsPesquisar.setEnabled(false);
        tblOsCli.setVisible(false);
    }

    private void alterar_os() {
        String sql = "Update tbos set tipo=?, situacao=?,equipamento=?,defeito=?,servico=?,tecnico=?,valor=? where os=?";
        try {
            conexao = ModuloConexao.conector();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cbxOsSit.getSelectedItem().toString());
            pst.setString(3, txtOsEquip.getText());
            pst.setString(4, txtOsDef.getText());
            pst.setString(5, txtOsServ.getText());
            pst.setString(6, txtOsTec.getText());
            //.replace substitui a virgula pelo ponto.
            pst.setString(7, txtOsVal.getText().replace(",", "."));
            pst.setString(8, txtOsNº.getText());

            //validação
            if (((((((txtOsId.getText().isEmpty() || txtOsEquip.getText().isEmpty() || txtOsDef.getText().isEmpty() || txtOsServ.getText().isEmpty() || txtOsTec.getText().isEmpty() || txtOsVal.getText().isEmpty() || txtOsCpf.getText().isEmpty()))))))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Alteração realizado com sucesso");
                    limpar();
                    reabilitação();

                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    // o metodo abaixo reabilita os botões criar, pesquisar(barra de digitação) e habilita a tabela de cliente, isso depois de uma operação de  e alteração 
    private void reabilitação() {
        btbOsCreate.setEnabled(true);
        txtOsPesquisar.setEnabled(true);
        tblOsCli.setVisible(true);
    }

    private void apagar_os() {

        try {

            if ((((((txtOsId.getText().isEmpty() || txtOsEquip.getText().isEmpty() || txtOsDef.getText().isEmpty() || txtOsServ.getText().isEmpty() || txtOsTec.getText().isEmpty() || txtOsVal.getText().isEmpty())))))) {
                JOptionPane.showMessageDialog(null, "Nenhum cliente selecionado");

            } else {

                int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir essa Os?", "Atenção", JOptionPane.YES_NO_OPTION);
                if (confirma == JOptionPane.YES_OPTION) {
                    String sql = "delete from tbos where os=?";

                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtOsNº.getText());
                    int apagado = pst.executeUpdate();
                    if (apagado > 0) {
                        JOptionPane.showMessageDialog(null, "Os excluida com sucesso");
                        limpar();
                        reabilitação();

                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void imprimir_os() {

        try {
            if ((((((txtOsId.getText().isEmpty() || txtOsEquip.getText().isEmpty() || txtOsDef.getText().isEmpty() || txtOsServ.getText().isEmpty() || txtOsTec.getText().isEmpty() || txtOsVal.getText().isEmpty())))))) {
                JOptionPane.showMessageDialog(null, "Nenhum cliente selecionado");
            } else {
                int confirma = JOptionPane.showConfirmDialog(null, "Confirma a emissão deste relatório?", "Atenção!", JOptionPane.YES_NO_OPTION);
                if (confirma == JOptionPane.YES_OPTION) {
                    //usando a classe para hashmap 
                    HashMap filtro = new HashMap();
                    filtro.put("os", Integer.valueOf(txtOsNº.getText()));

                    JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/Os.jasper"), filtro, conexao);

                    JasperViewer.viewReport(print, false);
                }
            }
            }catch (JRException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        rbtntOsOrca = new javax.swing.JRadioButton();
        rbtnOsOrdem = new javax.swing.JRadioButton();
        txtOsNº = new javax.swing.JTextField();
        txtOsData = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbxOsSit = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        txtOsPesquisar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtOsId = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOsCli = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        txtOsCpf = new javax.swing.JTextField();
        btbOsPrint = new javax.swing.JButton();
        btbOsDelete = new javax.swing.JButton();
        btbOsUpdate = new javax.swing.JButton();
        btbOsRead = new javax.swing.JButton();
        btbOsCreate = new javax.swing.JButton();
        txtOsVal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtOsEquip = new javax.swing.JTextField();
        txtOsDef = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtOsTec = new javax.swing.JTextField();
        txtOsServ = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Os");
        setPreferredSize(new java.awt.Dimension(610, 621));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Nº Os");

        jLabel2.setText("Data");

        buttonGroup1.add(rbtntOsOrca);
        rbtntOsOrca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbtntOsOrca.setText("Orçamento");
        rbtntOsOrca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtntOsOrcaActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtnOsOrdem);
        rbtnOsOrdem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbtnOsOrdem.setText("Ordem de serviço");
        rbtnOsOrdem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnOsOrdemActionPerformed(evt);
            }
        });

        txtOsNº.setEditable(false);

        txtOsData.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbtntOsOrca)
                            .addComponent(txtOsNº, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtOsData, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rbtnOsOrdem))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(51, 51, 51))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOsData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOsNº, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtntOsOrca)
                    .addComponent(rbtnOsOrdem))
                .addGap(21, 21, 21))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Situação");

        cbxOsSit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nenhum", "Entrega OK", "Orçamento reprovado", "Aguardando aprovação", "Aguardando peças", "Abandonado pelo cliente", "Na bancada", "Retornou" }));
        cbxOsSit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOsSitActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        txtOsPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOsPesquisarKeyReleased(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/pesquisar.png"))); // NOI18N

        txtOsId.setEditable(false);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Id");

        tblOsCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Fone", "CPF"
            }
        ));
        tblOsCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOsCliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblOsCli);

        jLabel11.setText("CPF");

        txtOsCpf.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtOsCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtOsId, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(txtOsPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(117, 117, 117))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtOsId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtOsCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtOsPesquisar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btbOsPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/print.png"))); // NOI18N
        btbOsPrint.setToolTipText("Imprimir cliente");
        btbOsPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbOsPrintActionPerformed(evt);
            }
        });

        btbOsDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        btbOsDelete.setToolTipText("Deletar");
        btbOsDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbOsDeleteActionPerformed(evt);
            }
        });

        btbOsUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        btbOsUpdate.setToolTipText("Atualizar");
        btbOsUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbOsUpdateActionPerformed(evt);
            }
        });

        btbOsRead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/read.png"))); // NOI18N
        btbOsRead.setToolTipText("Consultar");
        btbOsRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbOsReadActionPerformed(evt);
            }
        });

        btbOsCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btbOsCreate.setToolTipText("Adicionar");
        btbOsCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbOsCreateActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Serviço");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Técnico");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Valor total");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Equipamento");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Defeito");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/icons8-actualizar-64.png"))); // NOI18N
        jButton1.setToolTipText("Recarregar página");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxOsSit, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtOsServ)
                    .addComponent(txtOsEquip, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(1, 1, 1))
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtOsTec)
                    .addComponent(txtOsDef, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btbOsCreate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btbOsRead))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btbOsDelete)
                                .addGap(18, 18, 18)
                                .addComponent(btbOsPrint)))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btbOsUpdate)
                            .addComponent(jButton1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOsVal, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cbxOsSit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtOsEquip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtOsServ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtOsDef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(55, 55, 55)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtOsTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10)))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOsVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btbOsCreate)
                    .addComponent(btbOsRead)
                    .addComponent(btbOsUpdate))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addComponent(btbOsPrint)
                    .addComponent(btbOsDelete))
                .addGap(58, 58, 58))
        );

        setBounds(0, 0, 650, 621);
    }// </editor-fold>//GEN-END:initComponents

    private void cbxOsSitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxOsSitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxOsSitActionPerformed

    private void btbOsDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbOsDeleteActionPerformed
        // Chama o método remover
        apagar_os();
    }//GEN-LAST:event_btbOsDeleteActionPerformed

    private void btbOsUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbOsUpdateActionPerformed
        // chama metodo de alterar
        alterar_os();
    }//GEN-LAST:event_btbOsUpdateActionPerformed

    private void btbOsReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbOsReadActionPerformed
        //quando clica nele ele faz a leitura do id solicitado
        pesquisar_os();

    }//GEN-LAST:event_btbOsReadActionPerformed

    private void btbOsCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbOsCreateActionPerformed
        //chama o metodo emitir_os
        emitir_os();

    }//GEN-LAST:event_btbOsCreateActionPerformed

    private void txtOsPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsPesquisarKeyReleased
        // chamando o método pesquisar cliente
        pesquisar_cliente();
    }//GEN-LAST:event_txtOsPesquisarKeyReleased

    private void tblOsCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOsCliMouseClicked
        // Chamar metódo para setar campos
        setar_campos();
    }//GEN-LAST:event_tblOsCliMouseClicked

    private void rbtntOsOrcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtntOsOrcaActionPerformed
        // atribuindo um texto a variavel tippo se selecionado
        tipo = "Orçamento";
    }//GEN-LAST:event_rbtntOsOrcaActionPerformed

    private void rbtnOsOrdemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnOsOrdemActionPerformed
        // atribuindo um texto a variavel tippo se selecionado
        tipo = "Ordem de serviço";
    }//GEN-LAST:event_rbtnOsOrdemActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // ao abrir o form marcar o radio nutton Orçamento
        rbtntOsOrca.setSelected(true);
        tipo = "Orçamento";

    }//GEN-LAST:event_formInternalFrameOpened

    private void btbOsPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbOsPrintActionPerformed
        // 
        imprimir_os();


    }//GEN-LAST:event_btbOsPrintActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Chama o metódo limpar
        limpar();
        reabilitação();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btbOsCreate;
    private javax.swing.JButton btbOsDelete;
    private javax.swing.JButton btbOsPrint;
    private javax.swing.JButton btbOsRead;
    private javax.swing.JButton btbOsUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxOsSit;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbtnOsOrdem;
    private javax.swing.JRadioButton rbtntOsOrca;
    private javax.swing.JTable tblOsCli;
    private javax.swing.JTextField txtOsCpf;
    private javax.swing.JTextField txtOsData;
    private javax.swing.JTextField txtOsDef;
    private javax.swing.JTextField txtOsEquip;
    private javax.swing.JTextField txtOsId;
    private javax.swing.JTextField txtOsNº;
    private javax.swing.JTextField txtOsPesquisar;
    private javax.swing.JTextField txtOsServ;
    private javax.swing.JTextField txtOsTec;
    private javax.swing.JTextField txtOsVal;
    // End of variables declaration//GEN-END:variables
}
