var TemplateBox = React.createClass({
  getInitialState: function() {
    return {data: [], selectedTemplate: ""};
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
        <TemplateForm selectedTemplate={this.state.selectedTemplate} />
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
        {this.props.name}
      </button>
    );
  }
});

var TemplateForm = React.createClass({
  getInitialState: function() {
    return {data: []};
  },
  xcomponentDidMount: function() {
    $.ajax({
      url: "/api/placeholders/" + this.props.selectedTemplate,
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
  render: function() {
        var inputNodes = this.state.data.map(function(varName) {
          return (
            <div className="form-group">
              <label for={varName}>{varName}</label>
              <input type="text" className="form-control" name={varName} id={varName} />
            </div>
          );
        });

    if (this.props.selectedTemplate != "") { this.xcomponentDidMount(); }
    else { return null; }
    return (
      <div className="panel panel-primary">
        <div className="panel-heading">
          <h3 className="panel-title">{this.props.selectedTemplate}</h3>
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
                <option value="txt">Text</option>
                <option value="docx">Word (.docx)</option>
              </select>
            </div>
            <div className="form-group">
              <label for="engine">Engine</label>
              <select id="engine" name="engine" className="form-control">
                <option value="libreoffice">LibreOffice</option>
                <option value="wkhtmltopdf">wkhtmltopdf</option>
                <option value="pandoc">Pandoc</option>
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
