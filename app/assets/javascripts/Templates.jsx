var TemplateBox = React.createClass({
  getInitialState: function() {
    return {data: [], selectedTemplate: "", placeholders: []};
  },
  componentDidMount: function() {
    $.ajax({
      url: this.props.url,
      dataType: 'json',
      cache: false,
      success: function(data) {
        this.setState({data: data});
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  updateSelectedTemplateHandler: function(name) {
    this.setState({selectedTemplate: name});
    this.updatePlaceholders(name);
  },
  updatePlaceholders: function(selectedTemplate) {
    $.ajax({
        url: "/api/placeholders/" + selectedTemplate,
        dataType: 'json',
        cache: false,
        success: function(data) {
          this.setState({placeholders: data});
        }.bind(this),
        error: function(xhr, status, err) {
          console.error(this.props.url, status, err.toString());
        }.bind(this)
    });
  },
  render: function() {
    return (
      <div>
        <div className="panel panel-primary">
          <div className="panel-heading">
            <h3 className="panel-title">Vorlagen</h3>
          </div>
          <TemplateList data={this.state.data} updateHandler={this.updateSelectedTemplateHandler} />
        </div>
        <TemplateForm selectedTemplate={this.state.selectedTemplate}
                      placeholders={this.state.placeholders} />
      </div>
    );
  }
});

var TemplateList = React.createClass({
  render: function() {
    var updateHandler = this.props.updateHandler
    var templateNodes = this.props.data.map(function(template) {
      return (
        <Template name={template.name} updateHandler={updateHandler} />
      );
    });
    return (
      <div className="list-group">
        {templateNodes}
      </div>
    );
  }
});

var Template = React.createClass({
  handleClick: function(event) {
    this.props.updateHandler(this.props.name);
  },
  render: function() {
    return (
      <button type="button" className="list-group-item" onClick={this.handleClick}>
        {this.props.name.replace(".mustache", "")}
      </button>
    );
  }
});

var TemplateForm = React.createClass({
  getInitialState: function() {
    return {data: []};
  },
  render: function() {
    if (this.props.selectedTemplate == "") { return null; }

    var inputNodes = this.props.placeholders.map(function(varName) {
      return (
        <div className="form-group">
          <label for={varName}>{varName}</label>
          <input type="text" className="form-control" name={varName} id={varName} />
        </div>
      );
    });

    return (
      <div className="panel panel-primary">
        <div className="panel-heading">
          <h3 className="panel-title">{this.props.selectedTemplate.replace(".mustache", "")}</h3>
        </div>
        <div className="panel-body">
          <form action={"/api/render/" + this.props.selectedTemplate} method="POST" className="inputForm">
            {inputNodes}
            <div className="form-group">
              <label for="format">Format</label>
              <select id="format" name="format" className="form-control">
                <option value="html">HTML</option>
                <option value="odt">OpenDocument (.odt)</option>
                <option selected="selected" value="pdf">PDF</option>
                <option value="png">PNG</option>
                <option value="txt">Text (.txt)</option>
                <option value="docx">Word (.docx)</option>
              </select>
            </div>
            <div className="form-group">
              <label for="engine">Engine</label>
              <select id="engine" name="engine" className="form-control">
                <option value="libreoffice">LibreOffice</option>
                <option value="pandoc">Pandoc</option>
                <option value="wkhtmltoimage">Wkhtmltoimage</option>
                <option value="wkhtmltopdf">Wkhtmltopdf</option>
              </select>
            </div>
            <input type="submit" className="btn btn-default" value="Dokument erstellen" />
          </form>
        </div>
      </div>
    );
  }
});

ReactDOM.render(
  <TemplateBox url="/api/templates" />,
  document.getElementById('templates')
);
