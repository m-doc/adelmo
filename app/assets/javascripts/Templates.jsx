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
      <div className="templateBox">
        <strong>Templates</strong>
        <TemplateList data={this.state.data} updateHandler={this.updateSelectedTemplateHandler} />
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
      <ul className="templateList">
        {templateNodes}
      </ul>
    );
  }
});

var Template = React.createClass({
  handleClick: function(event) {
    this.props.updateHandler(this.props.name);
  },
  render: function() {
    return (
      <li className="template" onClick={this.handleClick}>
        {this.props.name}
      </li>
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
            <input type="text" name={varName} placeholder={varName} />
          );
        });

    if (this.props.selectedTemplate != "") { this.xcomponentDidMount(); }
    return (
    <div>
      <span>Form {this.props.selectedTemplate}</span>
      <form action={"/api/render/" + this.props.selectedTemplate} method="POST" className="inputForm">
        {inputNodes}
        <input type="submit" value="Post" />
      </form>
    </div>
    );
  }
});

ReactDOM.render(
  <TemplateBox url="/api/templates" />,
  document.getElementById('templates')
);
