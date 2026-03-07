
document.addEventListener('DOMContentLoaded', function() {
    var h5Elements = document.querySelectorAll('h5');
    h5Elements.forEach(function(h5) {
        if (h5.textContent.includes('Curl request')) {
            var codeElement = h5.nextElementSibling.querySelector('code');
            if (codeElement) {
                var originalCurlCommand = codeElement.textContent;

                var sendButton = document.createElement('button');
                sendButton.textContent = '전송하기';
                sendButton.style.marginLeft = '10px';
                sendButton.style.padding = '5px';

                var editButton = document.createElement('button');
                editButton.textContent = '수정하기';
                editButton.style.marginLeft = '10px';
                editButton.style.padding = '5px';

                var cancelButton = document.createElement('button');
                cancelButton.textContent = '취소';
                cancelButton.style.marginLeft = '10px';
                cancelButton.style.padding = '5px';
                cancelButton.style.display = 'none';

                var resultDiv = document.createElement('div');
                resultDiv.style.marginTop = '10px';
                resultDiv.style.padding = '10px';
                resultDiv.style.border = '1px solid #ccc';
                resultDiv.style.backgroundColor = '#f9f9f9';

                codeElement.parentElement.appendChild(editButton);
                codeElement.parentElement.appendChild(sendButton);
                codeElement.parentElement.appendChild(cancelButton);
                codeElement.parentElement.appendChild(resultDiv);

                var fileInputsDiv = document.createElement('div');
                fileInputsDiv.style.marginTop = '10px';
                codeElement.parentElement.appendChild(fileInputsDiv);

                checkForFileOption(codeElement.textContent);

                function checkForFileOption(curlCommand) {
                    var formMatches = curlCommand.match(/-F '([^=]+)=@?([^']+);type=(image\/png|image\/jpeg|application\/pdf)'/g);
                    if (formMatches) {
                        fileInputsDiv.innerHTML = ''; // 기존의 파일 입력들을 제거
                        formMatches.forEach(function(formField) {
                            var match = formField.match(/-F '([^=]+)=@?([^']+);type=([^']+)'/);
                            var fieldName = match[1];
                            var filePath = match[2];
                            var fileType = match[3];

                            var fileLabel = document.createElement('label');
                            fileLabel.textContent = `${fieldName} (${fileType}) 파일 선택: `;
                            var fileInput = document.createElement('input');
                            fileInput.type = 'file';
                            fileInput.accept = fileType;
                            fileInput.dataset.fieldName = fieldName;
                            fileInput.style.marginBottom = '5px';

                            fileInputsDiv.appendChild(fileLabel);
                            fileInputsDiv.appendChild(fileInput);
                            fileInputsDiv.appendChild(document.createElement('br'));
                        });
                    } else {
                        fileInputsDiv.innerHTML = ''; // -F가 없는 경우 입력 필드를 모두 제거
                    }
                }

                codeElement.contentEditable = false;

                editButton.addEventListener('click', function() {
                    codeElement.contentEditable = true;
                    codeElement.style.border = '1px solid #000';
                    cancelButton.style.display = 'inline';
                    codeElement.focus();
                });

                cancelButton.addEventListener('click', function() {
                    codeElement.contentEditable = false;
                    codeElement.style.border = 'none';
                    codeElement.textContent = originalCurlCommand;
                    cancelButton.style.display = 'none';
                    // fileInputsDiv.innerHTML = ''; // 입력 필드도 초기화
                });

                sendButton.addEventListener('click', function() {
                    codeElement.contentEditable = false;
                    codeElement.style.border = 'none';
                    cancelButton.style.display = 'none';
                    var modifiedCurlCommand = codeElement.textContent;
                    executeCurlCommand(modifiedCurlCommand, resultDiv, fileInputsDiv);
                });

            }
        }
    });

    function executeCurlCommand(curlCommand, resultDiv, fileInputsDiv) {
        var urlMatch = curlCommand.match(/curl '([^']+)'/);
        var methodMatch = curlCommand.match(/-X (\w+)/);
        var headers = [];
        var data = null;
        var formData = new FormData();
        var isFormData = false;
        var method = 'GET';

        if (methodMatch) {
            method = methodMatch[1].toUpperCase();
        }

        if (urlMatch) {
            var url = urlMatch[1];

            var headerMatches = curlCommand.match(/-H '([^']+)'/g);
            if (headerMatches) {
                headers = headerMatches.map(function(header) {
                    return header.replace(/-H '|'/g, '');
                });
            }

            var dataMatch = curlCommand.match(/-d '([^']+)'/);
            if (dataMatch) {
                data = dataMatch[1];
            }

            var formMatches = curlCommand.match(/-F '([^=]+)=@?([^']+)'/g);
            if (formMatches) {
                isFormData = true;
                formMatches.forEach(function(formField) {
                    var match = formField.match(/-F '([^=]+)=@?([^']+)type=(.+)'/);
                    var fieldName = match[1];
                    var filePath = match[2];
                    var fileType = match[3];
                    var fileInput = fileInputsDiv.querySelector(`input[data-field-name="${fieldName}"]`);
                    var file = fileInput ? new Blob([fileInput?.files[0]], { type: fileType }) : null;
                    // var file = fileInput?.files[0]
                    if (file) {
                        formData.append(fieldName, file, file.name);
                    } else {
                        formData.append(fieldName, new Blob([filePath], { type: fileType }));
                    }
                });
            }

            var options = {
                method: method,
                headers: headers.reduce(function(acc, header) {
                    var parts = header.split(': ');
                    acc[parts[0]] = parts[1];
                    return acc;
                }, {})
            };

            if (isFormData) {
                options.body = formData;
                delete options.headers['Content-Type'];
            } else if (data) {
                var contentType = options.headers['Content-Type'] || options.headers['content-type'];
                if (contentType && contentType.includes('application/x-www-form-urlencoded')) {
                    options.body = new URLSearchParams(data);
                } else {
                    options.body = data;
                }
            }

            fetch(url, options)
                .then(response => response.text())
                .then(result => {
                    console.log('Request succeeded with response', result);
                    resultDiv.textContent = 'Response: ' + result;
                })
                .catch(error => {
                    console.log('Request failed', error);
                    resultDiv.textContent = 'Error: ' + error;
                });
        } else {
            console.error('Invalid curl command');
        }
    }
});
