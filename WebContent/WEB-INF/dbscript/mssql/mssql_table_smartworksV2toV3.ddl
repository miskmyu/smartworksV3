ALTER TABLE SWEventday alter column reltdperson varchar(4000);

ALTER TABLE SWDataRef alter column refRecordId varchar(4000);

ALTER TABLE prcprcinst alter column prctitle varchar(255); --prctitle�� �÷������ text_input�� �÷��������� 255�� ����
ALTER TABLE tsktask alter column tsktitle varchar(255); --tsktitle�� �÷������ text_input�� �÷��������� 255�� ����

-- ����
CREATE TABLE SwFolder (
	id varchar(50) NOT NULL,
	companyid varchar(100),
	parentid varchar(50),
	name varchar(255),
	disporder int,
	description varchar(4000),
	creator varchar(30),
	createdtime datetime,
	modifier varchar(30),
	modifiedtime datetime,
	tskWorkspaceId varchar(50),
	tskRefType varchar(50),
	primary key (id)
);

-- ��������
CREATE TABLE SwFolderFile (
	folderId varchar(50) NOT NULL,
	fileId varchar(50) NOT NULL,
	fileSeq int,
  	primary key(folderId, fileId)
);


-- SwOpinion Table (���)�� �÷� �߰�
ALTER TABLE SwOpinion add modifier varchar(30);
ALTER TABLE SwOpinion add modifiedTime datetime;


-- TO-DO : user_field �߰� �� �÷� ���� 4000���� ����

-- SWOrgUser locale, timeZone column add
ALTER TABLE SWOrgUser add locale varchar(20);
ALTER TABLE SWOrgUser add timeZone varchar(20);
ALTER TABLE SWOrgUser add nickName varchar(255);

-- Ŀ�´�Ƽ �׷�
CREATE TABLE SWOrgGroup (
	id	varchar(50) not null,
	companyId	varchar(50),
	name	varchar(100),
	groupLeader	varchar(50),
	groupType	varchar(1),
	status	varchar(1),
	picture varchar(100),
	description	varchar(4000),
	creator	varchar(50),
	createdTime	datetime,
	modifier	varchar(50),
	modifiedTime	datetime,
	primary key (id)
);

-- Ŀ�´�Ƽ �׷� ���
CREATE TABLE SWOrgGroupMember (
	groupId	varchar(50) not null,
	userId	varchar(50) not null,
	joinType	varchar(1),
	joinStatus	varchar(1),
	joinTime	datetime,
	outTime 	datetime,
	memberSeq	int,
	creator varchar(50),
	createdtime datetime ,
	modifier varchar(50),
	modifiedtime datetime ,
	primary key (groupId, userId)
);

ALTER TABLE prcprcinst add prcPackageId varchar(100);
ALTER TABLE prcprcinst add prcType varchar(100);

ALTER TABLE tsktask add isStartActivity varchar(10);
ALTER TABLE tsktask add tskFromRefType varchar(50);
ALTER TABLE tsktask add tskFromRefId varchar(100);

-- ����������ǿ� ��� �ҿ�ð� �÷� �߰�
ALTER TABLE apraprdef add dueDate varchar(100);

ALTER TABLE aprapr add aprDueDate varchar(100);

-- swdocgroup ������ tskInstanceId �߰�
ALTER TABLE swdocgroup add tskInstanceId varchar(50);

-- TO-DO : mode -> authmode 
exec sp_rename 'SWAuthResource.mode', 'authMode', 'column'
exec sp_rename 'SWAuthUser.mode', 'authMode', 'column'


-- TO-DO : ���̺�� �� �÷��� ���� lnklist, lnklistextprop, lnklistitem, lnkmap, lnkmapextprop, lnkvalue, lnkvalueextprop �� ��� lnk�� col�� ����

-- ���̺�� ����
sp_rename lnklist, ColList
sp_rename lnklistextprop, ColListExtProp
sp_rename lnklistitem, ColListItem
sp_rename lnkmap, ColMap
sp_rename lnkmapextprop, ColMapExtProp
sp_rename lnkvalue, ColValue
sp_rename lnkvalueextprop, ColValueExtProp

-- �÷��� ����
exec sp_rename 'ColList.lnkObjId', 'colObjId', 'column'
exec sp_rename 'ColList.lnkName', 'colName', 'column'
exec sp_rename 'ColList.lnkCreateUser', 'colCreateUser', 'column'
exec sp_rename 'ColList.lnkCreateDate', 'colCreateDate', 'column'
exec sp_rename 'ColList.lnkModifyUser', 'colModifyUser', 'column'
exec sp_rename 'ColList.lnkModifyDate', 'colModifyDate', 'column'
exec sp_rename 'ColList.lnkType', 'colType', 'column'
exec sp_rename 'ColList.lnkCorr', 'colCorr', 'column'
exec sp_rename 'ColList.lnkStatus', 'colStatus', 'column'
exec sp_rename 'ColList.lnkDesc', 'colDesc', 'column'

exec sp_rename 'ColListExtProp.lnkObjId', 'colObjId', 'column'
exec sp_rename 'ColListExtProp.lnkSeq', 'colSeq', 'column'

exec sp_rename 'ColListItem.lnkObjId', 'colObjId', 'column'
exec sp_rename 'ColListItem.lnkType', 'colType', 'column'
exec sp_rename 'ColListItem.lnkRef', 'colRef', 'column'
exec sp_rename 'ColListItem.lnkLabel', 'colLabel', 'column'
exec sp_rename 'ColListItem.lnkExpr', 'colExpr', 'column'
exec sp_rename 'ColListItem.lnkSeq', 'colSeq', 'column'

exec sp_rename 'ColMap.lnkObjId', 'colObjId', 'column'
exec sp_rename 'ColMap.lnkName', 'colName', 'column'
exec sp_rename 'ColMap.lnkCreateUser', 'colCreateUser', 'column'
exec sp_rename 'ColMap.lnkCreateDate', 'colCreateDate', 'column'
exec sp_rename 'ColMap.lnkModifyUser', 'colModifyUser', 'column'
exec sp_rename 'ColMap.lnkModifyDate', 'colModifyDate', 'column'
exec sp_rename 'ColMap.lnkType', 'colType', 'column'
exec sp_rename 'ColMap.lnkFromType', 'colFromType', 'column'
exec sp_rename 'ColMap.lnkFromRef', 'colFromRef', 'column'
exec sp_rename 'ColMap.lnkToType', 'colToType', 'column'
exec sp_rename 'ColMap.lnkToRef', 'colToRef', 'column'

exec sp_rename 'ColMapExtProp.lnkObjId', 'colObjId', 'column'
exec sp_rename 'ColMapExtProp.lnkSeq', 'colSeq', 'column'

exec sp_rename 'ColValue.lnkObjId', 'colObjId', 'column'
exec sp_rename 'ColValue.lnkName', 'colName', 'column'
exec sp_rename 'ColValue.lnkCreateUser', 'colCreateUser', 'column'
exec sp_rename 'ColValue.lnkCreateDate', 'colCreateDate', 'column'
exec sp_rename 'ColValue.lnkModifyUser', 'colModifyUser', 'column'
exec sp_rename 'ColValue.lnkModifyDate', 'colModifyDate', 'column'
exec sp_rename 'ColValue.lnkType', 'colType', 'column'
exec sp_rename 'ColValue.lnkRef', 'colRef', 'column'
exec sp_rename 'ColValue.lnkExpDate', 'colExpDate', 'column'
exec sp_rename 'ColValue.lnkValue', 'colValue', 'column'
exec sp_rename 'ColValue.lnkStatus', 'colStatus', 'column'
exec sp_rename 'ColValue.lnkDesc', 'colDesc', 'column'

exec sp_rename 'ColValueExtProp.lnkObjId', 'colObjId', 'column'
exec sp_rename 'ColValueExtProp.lnkSeq', 'colSeq', 'column'


-- workspaceid column add
ALTER TABLE TskTask add tskWorkspaceId varchar(100);
ALTER TABLE TskTask add tskWorkSpaceType varchar(50);
ALTER TABLE TskTask add tskAccessLevel varchar(50);
ALTER TABLE TskTask add tskAccessValue varchar(4000);
ALTER TABLE TskTask add tskRefType varchar(100);
ALTER TABLE PrcprcInst add prcWorkspaceId varchar(100);
ALTER TABLE PrcprcInst add prcWorkSpaceType varchar(50);
ALTER TABLE PrcprcInst add prcAccessLevel varchar(50);
ALTER TABLE PrcprcInst add prcAccessValue varchar(4000);


--�ڷ��, �޸� ���̺� �÷��� ����

-- �Ʒ� --
-- * ����Ʈ ���� �ֿ� key ���� �ٸ� �� �ֱ� ������ ��ȸ �� �����ϰ� ����

-- �ڷ�� ���̺� �� ����
sp_rename dt_e2f7c08e0375498499b9a303ef212f53, SWAttachment

-- �ڷ�� domain ����
update swdomain set id='frm_attachment_SYSTEM', formid='frm_attachment_SYSTEM', tblname = 'SWAttachment' where formid = 'frm_148366628fb24edd976940398ba0d8d0'

-- �ڷ�� domainfield ����
update swdomainfield set domainid = 'frm_attachment_SYSTEM' where domainid= 'md_1aa3471d452b4914b4efd5176223a96d'
update swdomainfield set id = 'attachment_owneruser', tablecolname = 'owneruser' where domainid= 'frm_attachment_SYSTEM' and tablecolname = 'c3'
update swdomainfield set id = 'attachment_ownerdept', tablecolname = 'ownerdept'  where domainid= 'frm_attachment_SYSTEM' and tablecolname = 'c2'
update swdomainfield set id = 'attachment_content', tablecolname = 'content'  where domainid= 'frm_attachment_SYSTEM' and tablecolname = 'c4'
update swdomainfield set id = 'attachment_searchword', tablecolname = 'searchword'  where domainid= 'frm_attachment_SYSTEM' and tablecolname = 'c1'
update swdomainfield set id = 'attachment_title', tablecolname = 'title'  where domainid= 'frm_attachment_SYSTEM' and tablecolname = 'c0'
update swdomainfield set id = 'attachment_filegroupId', tablecolname = 'filegroupid'  where domainid= 'frm_attachment_SYSTEM' and tablecolname = 'c5'

update swform set formid = 'frm_attachment_SYSTEM', content = '<form id="frm_attachment_SYSTEM" version="1" name="�ڷ��">
	<children>
		<formEntity id="0" name="����" systemType="string" required="true" system="false">
			<children/>
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="80" contentWidth="190" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true"/>
		</formEntity>
		<formEntity id="1" name="�˻���" systemType="string" required="true" system="false">
			<children/>
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="80" contentWidth="190" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true"/>
		</formEntity>
		<formEntity id="2" name="�����μ�" systemType="string" required="true" system="false">
			<children/>
			<format type="refFormField" viewingType="refFormField">
				<refForm id="frm_dept_SYSTEM" ver="0">
					<name>null</name>
					<category id="null">null</category>
					<field id="10">null</field>
				</refForm>
			</format>
			<graphic hidden="false" readOnly="false" labelWidth="80" contentWidth="190" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true"/>
		</formEntity>
		<formEntity id="3" name="���������" systemType="string" required="true" system="false">
			<children/>
			<format type="userField" viewingType="userField"></format>
			<graphic hidden="false" readOnly="false" labelWidth="80" contentWidth="130" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="false"/>
		</formEntity>
		<formEntity id="4" name="����" systemType="text" required="false" system="false">
			<children/>
			<format type="richEditor" viewingType="richEditor"></format>
			<graphic hidden="false" readOnly="false" labelWidth="80" contentWidth="500" height="388" cellSize="1" fitWidth="false" verticalScrollPolicy="true"/>
		</formEntity>
		<formEntity id="5" name="÷������" systemType="string" required="true" system="false">
			<children/>
			<format type="fileField" viewingType="fileField"></format>
			<graphic hidden="false" readOnly="false" labelWidth="80" contentWidth="275" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true"/>
		</formEntity>
	</children>
	<mappingForms/>
	<layout type="grid_layout">
		<columns>
			<gridColumn size="358.5" labelSize="80"/>
			<gridColumn size="358.5" labelSize="80"/>
		</columns>
		<gridRow size="30">
			<gridCell size="358.5" span="2" fieldId="0"/>
		</gridRow>
		<gridRow size="30">
			<gridCell size="358.5" span="2" fieldId="1"/>
		</gridRow>
		<gridRow size="30">
			<gridCell size="358.5" span="1" fieldId="2"/>
			<gridCell size="358.5" span="1" fieldId="3"/>
		</gridRow>
		<gridRow size="395.5">
			<gridCell size="358.5" span="2" fieldId="4"/>
		</gridRow>
		<gridRow size="30">
			<gridCell size="358.5" span="2" fieldId="5"/>
		</gridRow>
	</layout>
	<graphic width="760" height="605" currentEntityNum="6" currentMappingNum="0">
		<space head="20" left="20" right="20" top="20" bottom="10"/>
	</graphic>
</form>'
where formid = 'frm_148366628fb24edd976940398ba0d8d0'


-- �޸� ���̺� �� ����
select * from dt_0d13bec72fcc43bca079eeb927bf1cc6
sp_rename dt_0d13bec72fcc43bca079eeb927bf1cc6, SWMemo

-- �޸� package ����
select * from swpackage where packageid = 'pkg_d391d4cd01864b2cada59ab5a9b12cd5'
update swpackage set name = '�޸�' where packageid = 'pkg_d391d4cd01864b2cada59ab5a9b12cd5'

-- �޸� domain ����
select * from swdomain where formid = 'frm_9d4df59b25694c8ea13e07e0f0fb2579'
update swdomain set id='frm_memo_SYSTEM', formid='frm_memo_SYSTEM', formName = '�޸�', tblname = 'SWMemo' where formid = 'frm_9d4df59b25694c8ea13e07e0f0fb2579'

-- �޸� domainfield ����
select * from swdomainfield where domainid= 'md_5aac6420321c44b282db0f2ee417a7f9'
update swdomainfield set domainid = 'frm_memo_SYSTEM' where domainid= 'md_5aac6420321c44b282db0f2ee417a7f9'
update swdomainfield set id = 'memo_title', tablecolname = 'title' where domainid= 'frm_memo_SYSTEM' and tablecolname = 'c12'
update swdomainfield set id = 'memo_content', tablecolname = 'content'  where domainid= 'frm_memo_SYSTEM' and tablecolname = 'c4'

select * from swform where formid = 'frm_9d4df59b25694c8ea13e07e0f0fb2579' 
update swform set formid = 'frm_memo_SYSTEM', name = '�޸�', content = '<form id="frm_memo_SYSTEM" version="1" name="�޸�">
	<children>
		<formEntity id="4" name="����" systemType="text" required="true" system="false">
			<children/>
			<format type="richEditor" viewingType="richEditor"></format>
			<graphic hidden="false" readOnly="false" labelWidth="80" contentWidth="500" height="604" cellSize="1" fitWidth="false" verticalScrollPolicy="true"/>
		</formEntity>
		<formEntity id="12" name="����" systemType="string" required="true" system="false">
			<children/>
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="80" contentWidth="190" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true"/>
		</formEntity>
	</children>
	<mappingForms/>
	<layout type="grid_layout">
		<columns>
			<gridColumn size="358.5" labelSize="80"/>
			<gridColumn size="358.5" labelSize="80"/>
		</columns>
		<gridRow size="30">
			<gridCell size="0" span="2" fieldId="12"/>
		</gridRow>
		<gridRow size="611">
			<gridCell size="358.5" span="2" fieldId="4"/>
		</gridRow>
	</layout>
	<graphic width="760" height="735" currentEntityNum="18" currentMappingNum="0">
		<space head="20" left="20" right="20" top="20" bottom="20"/>
	</graphic>
</form>'
where formid = 'frm_9d4df59b25694c8ea13e07e0f0fb2579'

select * from swauthresource where resourceId = 'frm_9d4df59b25694c8ea13e07e0f0fb2579'

update swauthresource set resourceId = 'frm_memo_SYSTEM' where resourceId = 'frm_9d4df59b25694c8ea13e07e0f0fb2579'

select * from swauthresource where resourceId = 'frm_148366628fb24edd976940398ba0d8d0'

update swauthresource set resourceId = 'frm_attachment_SYSTEM' where resourceId = 'frm_148366628fb24edd976940398ba0d8d0'


-- �ű� �߰�
INSERT INTO swpackage (id, categoryid, packageid, version, name, type, status, latestdeployedyn, creator, createdtime, modifier, modifiedtime, description) VALUES ('402880ec34e3d85d0134e3eb5768000b', '40288afb1b25f00b011b25f3c7950001', 'pkg_c08a02b36192489fbc13fdb6bed6f5fc', 1, '�̺�Ʈ', 'SINGLE', 'DEPLOYED', 'Y', 'admin', getdate(), 'admin', getdate(), '�̺�Ʈ');
INSERT INTO swpackage (id, categoryid, packageid, version, name, type, status, latestdeployedyn, creator, createdtime, modifier, modifiedtime, description) VALUES ('402880ec34e3d85d0134e3e13e9a0004', '40288afb1b25f00b011b25f3c7950001', 'pkg_62eeb90b11e1466b86d2d7c4dadf63ca', 1, '��������', 'SINGLE', 'DEPLOYED', 'Y', 'admin', getdate(), 'admin', getdate(), '��������');

INSERT INTO swdomain (id, companyId, formid, formversion, formname, tblowner, tblname, keycolumn, titlefieldid, masterid, masterfieldid, systemdomainyn, publishmode) VALUES ('frm_event_SYSTEM','Maninsoft', 'frm_event_SYSTEM', 1, '�̺�Ʈ', '', 'SWEvent', 'id', '0', NULL, NULL, 'Y', 'PUB_ALL');
INSERT INTO swdomain (id, companyId, formid, formversion, formname, tblowner, tblname, keycolumn, titlefieldid, masterid, masterfieldid, systemdomainyn, publishmode) VALUES ('frm_notice_SYSTEM','Maninsoft', 'frm_notice_SYSTEM', 1, '��������', '', 'SWNotice', 'id', '0', NULL, NULL, 'Y', 'PUB_ALL');

INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('event_content', 'frm_event_SYSTEM', '6', NULL, '�̺�Ʈ����', 'text', 'content', NULL, 'N', 'Y', NULL, 6, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('event_name', 'frm_event_SYSTEM', '0', NULL, '�̺�Ʈ�̸�', 'string', 'name', NULL, 'N', 'Y', NULL, 1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('event_start', 'frm_event_SYSTEM', '1', NULL, '��������', 'datetime', 'startdate', NULL, 'N', 'Y', NULL, 2, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('event_end', 'frm_event_SYSTEM', '2', NULL, '��������', 'datetime', 'enddate', NULL, 'N', 'Y', NULL, 3, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('event_alarm', 'frm_event_SYSTEM', '7', NULL, '�̸��˸�', 'string', 'alarm', NULL, 'N', 'Y', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('event_related_users', 'frm_event_SYSTEM', '5', NULL, '������', 'string', 'relatedusers', NULL, 'N', 'Y', NULL, 4, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('event_place', 'frm_event_SYSTEM', '4', NULL, '���', 'string', 'place', NULL, 'N', 'Y', NULL, 5, 0);

INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('notice_filegroupid', 'frm_notice_SYSTEM', '2', NULL, '÷������', 'string', 'filegroupid', NULL, 'N', 'Y', NULL, 2, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('notice_title', 'frm_notice_SYSTEM', '0', NULL, '����', 'string', 'title', NULL, 'N', 'Y', NULL, 1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('notice_content', 'frm_notice_SYSTEM', '1', NULL, '����', 'text', 'content', NULL, 'N', 'Y', NULL, 3, 0);

INSERT INTO swform (id, packageid, formid, version, name, type, status, keyword, creator, createdtime, modifier, modifiedtime, ownerdept, owner, encoding, description, content, publishmode) 
VALUES ('402880ec34e3d85d0134e3eb5768000c', 'pkg_c08a02b36192489fbc13fdb6bed6f5fc', 'frm_event_SYSTEM', 1, '�̺�Ʈ', 'SINGLE', 'DEPLOYED', NULL, 'admin', getdate(), 'admin', getdate(), NULL, NULL, NULL, '',
'<form id="frm_event_SYSTEM" version="1" name="�̺�Ʈ">
  <children>
    <formEntity id="0" name="�̺�Ʈ�̸�" systemType="string" required="true" system="false">
      <children/>
      <format type="textInput" viewingType="textInput"></format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="347" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="1" name="��������" systemType="datetime" required="true" system="false">
      <children/>
      <format type="dateTimeChooser" viewingType="dateTimeChooser">
        <date yearUse="false" sunNotUse="false" monNotUse="false" tueNotUse="false" wedNotUse="false" thuNotUse="false" friNotUse="false" satNotUse="false"/>
      </format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="237" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="2" name="��������" systemType="datetime" required="false" system="false">
      <children/>
      <format type="dateTimeChooser" viewingType="dateTimeChooser">
        <date yearUse="false" sunNotUse="false" monNotUse="false" tueNotUse="false" wedNotUse="false" thuNotUse="false" friNotUse="false" satNotUse="false"/>
      </format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="237" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="4" name="���" systemType="string" required="false" system="false">
      <children/>
      <format type="textInput" viewingType="textInput"></format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="347" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="5" name="������" systemType="string" required="false" system="false">
      <children/>
      <format type="userField" viewingType="userField"></format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="237" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="true"/>
    </formEntity>
    <formEntity id="6" name="�̺�Ʈ����" systemType="text" required="false" system="false">
      <children/>
      <format type="richEditor" viewingType="richEditor"></format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="915" height="259" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="7" name="�̸��˸�" systemType="string" required="false" system="false">
      <children/>
      <format type="comboBox" viewingType="comboBox">
        <list type="" refCodeCategoryId="null" refCodeCategoryName="null" listType="static">
          <staticItems>
            <staticItem>��������</staticItem>
            <staticItem>����</staticItem>
            <staticItem>5����</staticItem>
            <staticItem>10����</staticItem>
            <staticItem>15����</staticItem>
            <staticItem>30����</staticItem>
            <staticItem>�ѽð���</staticItem>
            <staticItem>�Ϸ���</staticItem>
          </staticItems>
        </list>
      </format>
      <graphic hidden="false" readOnly="false" labelWidth="79" contentWidth="189" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
  </children>
  <mappingForms/>
  <mappingServices/>
  <layout type="grid_layout">
    <columns>
      <gridColumn size="460.60179500795493" labelSize="100.73022128896787"/>
      <gridColumn size="460.60179500795493" labelSize="100.73022128896787"/>
      <gridColumn size="459.9547634680662" labelSize="79.67565821353577"/>
    </columns>
    <gridRow size="30">
      <gridCell size="692.5202713616541" span="3" rowSpan="1" gridColumnIndex="0" fieldId="0"/>
    </gridRow>
    <gridRow size="30">
      <gridCell size="692.5202713616541" span="1" rowSpan="1" gridColumnIndex="0" fieldId="1"/>
      <gridCell size="692.5202713616541" span="1" rowSpan="1" gridColumnIndex="1" fieldId="2"/>
      <gridCell size="99.59457276691973" span="1" rowSpan="1" gridColumnIndex="2" fieldId="7"/>
    </gridRow>
    <gridRow size="30">
      <gridCell size="692.5202713616541" span="3" rowSpan="1" gridColumnIndex="0" fieldId="4"/>
    </gridRow>
    <gridRow size="30">
      <gridCell size="692.5202713616541" span="3" rowSpan="1" gridColumnIndex="0" fieldId="5"/>
    </gridRow>
    <gridRow size="266">
      <gridCell size="692.5202713616541" span="3" rowSpan="1" gridColumnIndex="0" fieldId="6"/>
    </gridRow>
  </layout>
  <graphic width="1399" height="540" currentEntityNum="8" currentMappingNum="0">
    <space/>
  </graphic>
</form>', 'PUB_ALL');


INSERT INTO swform (id, packageid, formid, version, name, type, status, keyword, creator, createdtime, modifier, modifiedtime, ownerdept, owner, encoding, description, content, publishmode) 
VALUES ('402880ec34e3d85d0134e3e13ea00005', 'pkg_62eeb90b11e1466b86d2d7c4dadf63ca', 'frm_notice_SYSTEM', 1, '��������', 'SINGLE', 'DEPLOYED', NULL, 'admin', getdate(), 'admin', getdate(), NULL, NULL, NULL, '',
'<form id="frm_notice_SYSTEM" version="1" name="��������">
  <children>
    <formEntity id="0" name="����" systemType="string" required="true" system="false">
      <children/>
      <format type="textInput" viewingType="textInput"></format>
      <graphic hidden="false" readOnly="false" labelWidth="113" contentWidth="269" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="1" name="����" systemType="text" required="true" system="false">
      <children/>
      <format type="richEditor" viewingType="richEditor"></format>
      <graphic hidden="false" readOnly="false" labelWidth="113" contentWidth="709" height="354" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="2" name="÷������" systemType="string" required="false" system="false">
      <children/>
      <format type="fileField" viewingType="fileField"></format>
      <graphic hidden="false" readOnly="false" labelWidth="113" contentWidth="390" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
  </children>
  <mappingForms/>
  <mappingServices/>
  <layout type="grid_layout">
    <columns>
      <gridColumn size="536.5610859728507" labelSize="78.04524886877827"/>
      <gridColumn size="536.5610859728507" labelSize="78.04524886877827"/>
    </columns>
    <gridRow size="30">
      <gridCell size="536.5610859728507" span="2" rowSpan="1" gridColumnIndex="0" fieldId="0"/>
    </gridRow>
    <gridRow size="271">
      <gridCell size="536.5610859728507" span="2" rowSpan="4" gridColumnIndex="0" fieldId="1"/>
    </gridRow>
    <gridRow size="30"/>
    <gridRow size="30"/>
    <gridRow size="30"/>
    <gridRow size="30">
      <gridCell size="536.5610859728507" span="2" rowSpan="1" gridColumnIndex="0" fieldId="2"/>
    </gridRow>
  </layout>
  <graphic width="1080" height="577" currentEntityNum="3" currentMappingNum="0">
    <space/>
  </graphic>
</form>', 'PUB_ALL');


-- �̺�Ʈ ���̺� �� ����
sp_rename dt_1326675664565, SWEvent

-- �̺�Ʈ domain ����
update swdomain set id='frm_event_SYSTEM', formid='frm_event_SYSTEM', tblname = 'SWEvent' where formid = 'frm_a8b6f52fdd2f4cf6a4f7f3f9f175f4d3'

-- �̺�Ʈ domainfield ����

update swdomainfield set domainid = 'frm_event_SYSTEM' where domainid= 'md_8d94e6225bf74312a22fe9ba2ac98124'
update swdomainfield set id = 'event_content', tablecolname = 'content' where domainid= 'frm_event_SYSTEM' and tablecolname = 'c6'
update swdomainfield set id = 'event_name', tablecolname = 'name'  where domainid= 'frm_event_SYSTEM' and tablecolname = 'c0'
update swdomainfield set id = 'event_start', tablecolname = 'startdate'  where domainid= 'frm_event_SYSTEM' and tablecolname = 'c1'
update swdomainfield set id = 'event_end', tablecolname = 'enddate'  where domainid= 'frm_event_SYSTEM' and tablecolname = 'c2'
update swdomainfield set id = 'event_alarm', tablecolname = 'alarm'  where domainid= 'frm_event_SYSTEM' and tablecolname = 'c7'
update swdomainfield set id = 'event_related_users', tablecolname = 'relatedusers'  where domainid= 'frm_event_SYSTEM' and tablecolname = 'c5'
update swdomainfield set id = 'event_place', tablecolname = 'place'  where domainid= 'frm_event_SYSTEM' and tablecolname = 'c4'

update swform set formid = 'frm_event_SYSTEM', content = '<form id="frm_event_SYSTEM" version="1" name="�̺�Ʈ">
  <children>
    <formEntity id="0" name="�̺�Ʈ�̸�" systemType="string" required="true" system="false">
      <children/>
      <format type="textInput" viewingType="textInput"></format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="347" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="1" name="��������" systemType="datetime" required="true" system="false">
      <children/>
      <format type="dateTimeChooser" viewingType="dateTimeChooser">
        <date yearUse="false" sunNotUse="false" monNotUse="false" tueNotUse="false" wedNotUse="false" thuNotUse="false" friNotUse="false" satNotUse="false"/>
      </format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="237" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="2" name="��������" systemType="datetime" required="false" system="false">
      <children/>
      <format type="dateTimeChooser" viewingType="dateTimeChooser">
        <date yearUse="false" sunNotUse="false" monNotUse="false" tueNotUse="false" wedNotUse="false" thuNotUse="false" friNotUse="false" satNotUse="false"/>
      </format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="237" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="4" name="���" systemType="string" required="false" system="false">
      <children/>
      <format type="textInput" viewingType="textInput"></format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="347" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="5" name="������" systemType="string" required="false" system="false">
      <children/>
      <format type="userField" viewingType="userField"></format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="237" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="true"/>
    </formEntity>
    <formEntity id="6" name="�̺�Ʈ����" systemType="text" required="false" system="false">
      <children/>
      <format type="richEditor" viewingType="richEditor"></format>
      <graphic hidden="false" readOnly="false" labelWidth="146" contentWidth="915" height="259" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="7" name="�̸��˸�" systemType="string" required="false" system="false">
      <children/>
      <format type="comboBox" viewingType="comboBox">
        <list type="" refCodeCategoryId="null" refCodeCategoryName="null" listType="static">
          <staticItems>
            <staticItem>��������</staticItem>
            <staticItem>����</staticItem>
            <staticItem>5����</staticItem>
            <staticItem>10����</staticItem>
            <staticItem>15����</staticItem>
            <staticItem>30����</staticItem>
            <staticItem>�ѽð���</staticItem>
            <staticItem>�Ϸ���</staticItem>
          </staticItems>
        </list>
      </format>
      <graphic hidden="false" readOnly="false" labelWidth="79" contentWidth="189" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
  </children>
  <mappingForms/>
  <mappingServices/>
  <layout type="grid_layout">
    <columns>
      <gridColumn size="460.60179500795493" labelSize="100.73022128896787"/>
      <gridColumn size="460.60179500795493" labelSize="100.73022128896787"/>
      <gridColumn size="459.9547634680662" labelSize="79.67565821353577"/>
    </columns>
    <gridRow size="30">
      <gridCell size="692.5202713616541" span="3" rowSpan="1" gridColumnIndex="0" fieldId="0"/>
    </gridRow>
    <gridRow size="30">
      <gridCell size="692.5202713616541" span="1" rowSpan="1" gridColumnIndex="0" fieldId="1"/>
      <gridCell size="692.5202713616541" span="1" rowSpan="1" gridColumnIndex="1" fieldId="2"/>
      <gridCell size="99.59457276691973" span="1" rowSpan="1" gridColumnIndex="2" fieldId="7"/>
    </gridRow>
    <gridRow size="30">
      <gridCell size="692.5202713616541" span="3" rowSpan="1" gridColumnIndex="0" fieldId="4"/>
    </gridRow>
    <gridRow size="30">
      <gridCell size="692.5202713616541" span="3" rowSpan="1" gridColumnIndex="0" fieldId="5"/>
    </gridRow>
    <gridRow size="266">
      <gridCell size="692.5202713616541" span="3" rowSpan="1" gridColumnIndex="0" fieldId="6"/>
    </gridRow>
  </layout>
  <graphic width="1399" height="540" currentEntityNum="8" currentMappingNum="0">
    <space/>
  </graphic>
</form>'
where formid = 'frm_a8b6f52fdd2f4cf6a4f7f3f9f175f4d3'


-- �������� ���̺� �� ����
sp_rename dt_1326673665416, SWNotice

-- �������� domain ����
update swdomain set id='frm_notice_SYSTEM', formid='frm_notice_SYSTEM', tblname = 'SWNotice' where formid = 'frm_e5ebdd49311a4007a1abfe831ff68e64'

-- �������� domainfield ����

update swdomainfield set domainid = 'frm_notice_SYSTEM' where domainid= 'md_4d010542aabc45ff98e2f362026e71e0'
update swdomainfield set id = 'notice_filegroupid', tablecolname = 'filegroupid' where domainid= 'frm_notice_SYSTEM' and tablecolname = 'c2'
update swdomainfield set id = 'notice_title', tablecolname = 'title'  where domainid= 'frm_notice_SYSTEM' and tablecolname = 'c0'
update swdomainfield set id = 'notice_content', tablecolname = 'content'  where domainid= 'frm_notice_SYSTEM' and tablecolname = 'c1'

update swform set formid = 'frm_notice_SYSTEM', content = '<form id="frm_notice_SYSTEM" version="1" name="��������">
  <children>
    <formEntity id="0" name="����" systemType="string" required="true" system="false">
      <children/>
      <format type="textInput" viewingType="textInput"></format>
      <graphic hidden="false" readOnly="false" labelWidth="113" contentWidth="269" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="1" name="����" systemType="text" required="true" system="false">
      <children/>
      <format type="richEditor" viewingType="richEditor"></format>
      <graphic hidden="false" readOnly="false" labelWidth="113" contentWidth="709" height="354" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
    <formEntity id="2" name="÷������" systemType="string" required="false" system="false">
      <children/>
      <format type="fileField" viewingType="fileField"></format>
      <graphic hidden="false" readOnly="false" labelWidth="113" contentWidth="390" height="23" cellSize="1" fitWidth="false" verticalScrollPolicy="true" textAlign="left" fitToScreen="false" listEditable="false" multipleUsers="false"/>
    </formEntity>
  </children>
  <mappingForms/>
  <mappingServices/>
  <layout type="grid_layout">
    <columns>
      <gridColumn size="536.5610859728507" labelSize="78.04524886877827"/>
      <gridColumn size="536.5610859728507" labelSize="78.04524886877827"/>
    </columns>
    <gridRow size="30">
      <gridCell size="536.5610859728507" span="2" rowSpan="1" gridColumnIndex="0" fieldId="0"/>
    </gridRow>
    <gridRow size="271">
      <gridCell size="536.5610859728507" span="2" rowSpan="4" gridColumnIndex="0" fieldId="1"/>
    </gridRow>
    <gridRow size="30"/>
    <gridRow size="30"/>
    <gridRow size="30"/>
    <gridRow size="30">
      <gridCell size="536.5610859728507" span="2" rowSpan="1" gridColumnIndex="0" fieldId="2"/>
    </gridRow>
  </layout>
  <graphic width="1080" height="577" currentEntityNum="3" currentMappingNum="0">
    <space/>
  </graphic>
</form>'
where formid = 'frm_e5ebdd49311a4007a1abfe831ff68e64'

--���ϰ��� ���̺� Start
CREATE TABLE folder_db_objects (
    id bigint IDENTITY(1,1) NOT NULL,
    username varchar(255) NOT NULL,
    parent_id bigint NOT NULL,
    folder_name varchar(100) NOT NULL,
    folder_type int NOT NULL,
    primary key (id)
);

CREATE TABLE msg_db_objects (
    id bigint IDENTITY(1,1) NOT NULL,
    username varchar(255) NOT NULL,
    folder_id bigint NOT NULL,
    unique_id varchar(100) NOT NULL,
    sender varchar(255),
    receiver text,
    cc text,
    bcc text,
    replyTo varchar(255),
    subject varchar(255),
    multipart smallint,
    priority int,
    sentdate datetime,
    unread smallint NOT NULL,
    msg_size bigint NOT NULL,
    email varbinary(max) NOT NULL,
    uid varchar(100)
    primary key (id)
);

CREATE TABLE msg_db_uids (
    username varchar(255) NOT NULL,
    uid varchar(100) NOT NULL
);

CREATE TABLE msg_rules (
    id bigint IDENTITY(1,1) NOT NULL,
    username varchar(255) NOT NULL,
    portion varchar(100) NOT NULL,
    rule_condition varchar(30) NOT NULL,
    keyword varchar(255) NOT NULL,
    rule_action varchar(30) NOT NULL,
    destination varchar(100) NOT NULL,
    primary key (id)
);
--���ϰ��� ���̺� End

--������� ���� ���� �׼� ���� ����
ALTER TABLE swfile add deleteAction bit;


--���� ���� ���� ���Ǵϼ��� �����ϱ����� �÷�
alter table apraprline add aprRefAppLineDefId varchar(50);

--Ű�� �ߺ� ��� ����
alter table swdomain add keyDuplicable bit;
--alter table swdomain add constraint swdomain_df default 1 for keyDuplicable

-- keyColumn = Ű�ʵ�, titleFieldId = �����ʵ�
update swdomain set keyColumn = titleFieldId


-- ���ϻ�뿩��
ALTER TABLE SWOrgUser add useMail bit;
UPDATE SWOrgUser SET useMail = 0;


-- ���λ�뿩��
ALTER TABLE SWOrgUser add useSign bit;
UPDATE SWOrgUser SET useSign = 0;

-- ���θ�
ALTER TABLE SWOrgUser add sign varchar(50);


-- SwMailServer (���ϼ�������)
CREATE TABLE SwMailServer (
	id varchar(50) NOT NULL,
	name varchar(100) NOT NULL,
	companyId varchar(50) NOT NULL,
	fetchServer varchar(50),
	fetchServerPort int,
	fetchProtocol varchar(10),
	fetchSsl bit,
	smtpServer varchar(50),
	smtpServerPort int,
	smtpAuthenticated bit,
	smtpSsl bit,
	creator	varchar(50),
	createdtime datetime,
	modifier varchar(50),
	modifiedtime datetime,
    primary key (id)
);

-- SwMailAccount (����ڸ��ϰ���)
CREATE TABLE SwMailAccount (
	id varchar(50) NOT NULL,
	userId varchar(50) NOT NULL,
	mailServerId varchar(50) NOT NULL,
	mailServerName varchar(100) NOT NULL,
	mailId varchar(50) NOT NULL,
	mailUserNameId varchar(50) NOT NULL,
	mailPassword varchar(50) NOT NULL,
	creator	varchar(50),
	createdtime datetime,
	modifier varchar(50),
	modifiedtime datetime,
    primary key (id)
);

-- ȸ�� �α��������� �̹���
ALTER TABLE SWConfig add loginImage varchar(50);

-- ���ڰ���, �������� ���̵�

alter table tsktask add tskApprovalId varchar(100)

alter table tsktask add tskForwardId varchar(100)

alter table tsktask add tskIsApprovalSourceTask varchar(10)

alter table tsktask add tskTargetApprovalStatus varchar(10)

ALTER TABLE sworgdept add picture varchar(50);

ALTER TABLE sworggroup add maxMember int;

ALTER TABLE sworggroup add autoApproval bit;

update sworggroup set maxmember='-1'

update sworggroup set autoApproval='0'


ALTER TABLE SWpackage add helpUrl varchar(500);

ALTER TABLE SWpackage add manualFileName varchar(100);

ALTER TABLE SwOrgUser add adjunctDeptIds varchar(500);

ALTER TABLE SwMailAccount add mailUserName varchar(50);

ALTER TABLE SwMailAccount add mailDeleteFetched varchar(10);

ALTER TABLE aprapr add aprDueDate character varying(100);


alter table msg_db_uids alter column uid varchar(100) collate Korean_Wansung_CS_AS

CREATE TABLE SWFileDownHistory (
	id varchar(50) NOT NULL,
	fileId varchar(50),
	fileName varchar(255),
	downloadUserId varchar(50),
	refPackageId varchar(100),
	refPackageName varchar(255),
	refPrcInstId varchar(100),
	refPrcInstName varchar(200),
	refTaskId varchar(50),
	refTaskName varchar(200),
	creator	varchar(50),
	createdtime datetime,
	modifier varchar(50),
	modifiedtime datetime,
    primary key (id)
);

CREATE TABLE SwLoginUserHistory (
	id varchar(50) NOT NULL,
	userId varchar(50),
	loginTime datetime,
    primary key (id)
);

CREATE TABLE SwAutoIndexInst (
	objId varchar(50) NOT NULL,
	instanceId varchar(100),
	formId varchar(100),
	fieldId varchar(10),
	refType varchar(10),
	idType varchar(50),
	idValue varchar(200),
	seq int,
	creator	varchar(50),
	createdtime datetime,
	modifier varchar(50),
	modifiedtime datetime,
    primary key (objId)
);

ALTER TABLE swmailaccount add mailSignature character varying(4000);
ALTER TABLE swmailaccount add useMailSign bit;
update swmailaccount set useMailSign='0'

ALTER TABLE swmailaccount add senderUserTitle varchar(50);

ALTER TABLE sworgconfig add useMessagingService bit;
update sworgconfig set useMessagingService='0'

ALTER TABLE sworgconfig add useChattingService bit;
update sworgconfig set useChattingService='0'

ALTER TABLE SwMailServer add pwChangeAPI character varying(300);
ALTER TABLE SwMailServer add pwChangeDefaultData character varying(100);
ALTER TABLE SwMailServer add pwChangeParamId character varying(50);
ALTER TABLE SwMailServer add pwChangeParamOldPW character varying(50);
ALTER TABLE SwMailServer add pwChangeParamNewPW character varying(50);

