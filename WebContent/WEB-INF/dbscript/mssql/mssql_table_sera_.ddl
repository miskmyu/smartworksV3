CREATE TABLE CourseDetail (
	courseId varchar(50) NOT NULL,
	object varchar(500),
	categories varchar(500),
	keywords varchar(500),
	duration int,
	startDate datetime,
	endDate datetime,
	maxMentees int,
	autoApproval tinyint,
	payable tinyint,
	fee int,
	teamId varchar(100),
	targetPoint int,
	achievedPoint int,
	lastMissionIndex int,
	primary key (courseId)
);
CREATE TABLE MentorDetail (
	mentorId varchar(50) NOT NULL,
	born varchar(500),
	homeTown varchar(500),
	living varchar(500),
	family varchar(100),
	works varchar(100),
	mentorHistory varchar(500),
	menteeHistory varchar(500),
	lectures varchar(500),
	awards varchar(500),
	etc varchar(500),
	educations varchar(500),
	primary key (mentorId)
);

--Mission Tabel
CREATE TABLE Mission (
	id varchar(50)NOT NULL,
	domainid varchar(50),
	workitemid varchar(50),
	masterrecordid varchar(50),
	creator varchar(50),
	createdtime datetime ,
	modifier varchar(50),
	modifiedtime datetime ,
	missionClearers varchar(255),
	closeDate datetime ,
	openDate datetime ,
	prevMission varchar(255),
	idx varchar(255),
	title varchar(255),
	starPoint varchar(255),
	content text,
	hit int,
	workSpaceId varchar(100),
	workSpaceType varchar(50),
	accessLevel varchar(1),
	accessValue varchar(4000),
	primary key (id)
);
INSERT INTO swpackage (id, categoryid, packageid, version, name, type, status, latestdeployedyn, creator, createdtime, modifier, modifiedtime, description) VALUES ('402880e3368c3af901368c3ccd690004', '40288afb1b25f00b011b25f3c7950001', 'pkg_dc3edb6efa47418cbd1f8fef889b4818', 1, '미션관리', 'SINGLE', 'DEPLOYED', 'Y', 'admin', getdate(), 'admin', getdate(), '미션관리');

INSERT INTO swdomain (id, companyId, formid, formversion, formname, tblowner, tblname, keycolumn, titlefieldid, masterid, masterfieldid, systemdomainyn, publishmode) VALUES ('sera_mission','Maninsoft', 'sera_mission', 1, '미션관리', '', 'Mission', 'id', '7', NULL, NULL, 'Y', 'PUB_ALL');

INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_content', 'sera_mission', '6', NULL, 'content', 'text', 'content', NULL, 'N', 'N', NULL, 5, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_missionClearers', 'sera_mission', '4', NULL, 'missionClearers', 'string', 'missionClearers', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_closeDate', 'sera_mission', '3', NULL, 'closeDate', 'datetime', 'closeDate', NULL, 'N', 'N', NULL, 1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_prevMission', 'sera_mission', '1', NULL, 'prevMission', 'string', 'prevMission', NULL, 'N', 'N', NULL, 3, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_index', 'sera_mission', '0', NULL, 'index', 'string', 'idx', NULL, 'N', 'N', NULL, 4, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_title', 'sera_mission', '7', NULL, 'title', 'string', 'title', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_starPoint', 'sera_mission', '5', NULL, 'starPoint', 'string', 'starPoint', NULL, 'N', 'N', NULL, 6, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_openDate', 'sera_mission', '2', NULL, 'openDate', 'datetime', 'openDate', NULL, 'N', 'N', NULL, 2, 0);

INSERT INTO swform (id, packageid, formid, version, name, type, status, creator, createdtime, modifier, modifiedtime, content)
VALUES ('402880e3368c3af901368c3ccd6e0005', 'pkg_dc3edb6efa47418cbd1f8fef889b4818', 'sera_mission', 1, '미션관리', 'SINGLE', 'DEPLOYED', 'admin', getdate(), 'admin', getdate(), '<form id="sera_mission" version="1" name="미션관리">
	<children>
		<formEntity id="0" name="index" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="293" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="1" name="prevMission" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="293" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="2" name="openDate" systemType="datetime"
			required="false" system="false">
			<children />
			<format type="dateChooser" viewingType="dateChooser">
				<date yearUse="false" sunNotUse="false" monNotUse="false"
					tueNotUse="false" wedNotUse="false" thuNotUse="false" friNotUse="false"
					satNotUse="false" />
			</format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="201" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="3" name="closeDate" systemType="datetime"
			required="false" system="false">
			<children />
			<format type="dateChooser" viewingType="dateChooser">
				<date yearUse="false" sunNotUse="false" monNotUse="false"
					tueNotUse="false" wedNotUse="false" thuNotUse="false" friNotUse="false"
					satNotUse="false" />
			</format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="201" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="4" name="missionClearers" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="293" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="5" name="starPoint" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="293" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="6" name="content" systemType="text"
			required="false" system="false">
			<children />
			<format type="richEditor" viewingType="richEditor"></format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="773" height="83" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="7" name="title" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="79"
				contentWidth="189" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
	</children>
	<mappingForms />
	<mappingServices />
	<layout type="grid_layout">
		<columns>
			<gridColumn size="586.0215298303115" labelSize="77.87661525984205" />
			<gridColumn size="586.0215298303115" labelSize="77.87661525984205" />
		</columns>
		<gridRow size="30">
			<gridCell size="586.0215298303115" span="2" rowSpan="1"
				gridColumnIndex="0" fieldId="7" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="0" />
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="1" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="2" />
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="3" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="4" />
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="5" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0215298303115" span="2" rowSpan="3"
				gridColumnIndex="0" fieldId="6" />
		</gridRow>
		<gridRow size="30" />
		<gridRow size="30" />
	</layout>
	<graphic width="1184" height="392" currentEntityNum="8"
		currentMappingNum="0">
		<space />
	</graphic>
</form>');





